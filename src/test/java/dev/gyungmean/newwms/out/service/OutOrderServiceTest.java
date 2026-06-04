package dev.gyungmean.newwms.out.service;

import dev.gyungmean.newwms.inventory.domain.Stock;
import dev.gyungmean.newwms.inventory.domain.vo.ReservationStatus;
import dev.gyungmean.newwms.inventory.repository.StockRepository;
import org.springframework.test.util.ReflectionTestUtils;
import dev.gyungmean.newwms.inventory.service.FifoCandidateSelector;
import dev.gyungmean.newwms.inventory.service.FifoCandidateSelector.FifoAllocation;
import dev.gyungmean.newwms.inventory.service.FifoCandidateSelector.FifoSelection;
import dev.gyungmean.newwms.master.domain.BagType;
import dev.gyungmean.newwms.out.domain.*;
import dev.gyungmean.newwms.out.repository.OutOrderRepository;
import dev.gyungmean.newwms.out.repository.OutPlanDetailRepository;
import dev.gyungmean.newwms.out.repository.OutPlanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OutOrderServiceTest {

    @Mock private OutPlanRepository outPlanRepository;
    @Mock private OutPlanDetailRepository outPlanDetailRepository;
    @Mock private OutOrderRepository outOrderRepository;
    @Mock private StockRepository stockRepository;
    @Mock private FifoCandidateSelector fifoCandidateSelector;

    @InjectMocks
    private OutOrderService outOrderService;

    // ========== placeOrder (FIFO 자동 출고지시) ==========

    @Test
    @DisplayName("FIFO 자동 출고지시 - 단일 detail, 재고 2개에서 분할 할당")
    void placeOrder_fifo_splitAllocation() {
        OutPlan plan = createPlan("ORD0000001");
        plan.addDetail("ITEM001", null, "L1", BigDecimal.valueOf(150), "TON", "S001", "0001");
        OutPlanDetail detail = plan.getDetails().get(0);

        Stock s1 = createStock(1L, BigDecimal.valueOf(100), LocalDate.of(2024, 1, 1));
        Stock s2 = createStock(2L, BigDecimal.valueOf(100), LocalDate.of(2024, 2, 1));
        List<Stock> candidates = List.of(s1, s2);

        FifoSelection selection = new FifoSelection(List.of(
                new FifoAllocation(s1, BigDecimal.valueOf(100)),
                new FifoAllocation(s2, BigDecimal.valueOf(50))
        ));

        given(outPlanRepository.findById("ORD0000001")).willReturn(Optional.of(plan));
        given(stockRepository.findByItemCodeAndStockStatusNotOrderByLotDateAsc(any(), any()))
                .willReturn(candidates);
        given(fifoCandidateSelector.select(candidates, detail.getOrderQty())).willReturn(selection);
        given(stockRepository.findById(1L)).willReturn(Optional.of(s1));
        given(stockRepository.findById(2L)).willReturn(Optional.of(s2));
        given(outOrderRepository.save(any(OutOrder.class))).willAnswer(i -> i.getArgument(0));

        List<OutOrder> orders = outOrderService.placeOrder("ORD0000001");

        assertThat(orders).hasSize(2);
        assertThat(orders.get(0).getAllocatedQty()).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(orders.get(1).getAllocatedQty()).isEqualByComparingTo(BigDecimal.valueOf(50));
        assertThat(s1.getReservationStatus()).isEqualTo(ReservationStatus.RESERVED);
        assertThat(s2.getReservationStatus()).isEqualTo(ReservationStatus.RESERVED);
    }

    @Test
    @DisplayName("FIFO 자동 출고지시 - 여러 detail이면 detail 수만큼 FIFO 선정")
    void placeOrder_fifo_multipleDetails() {
        OutPlan plan = createPlan("ORD0000002");
        plan.addDetail("ITEM001", null, "L1", BigDecimal.valueOf(100), "TON", "S001", "0001");
        plan.addDetail("ITEM002", null, "L1", BigDecimal.valueOf(50), "TON", "S001", "0001");

        Stock s1 = createStock(1L, BigDecimal.valueOf(100), LocalDate.of(2024, 1, 1));
        Stock s2 = createStock(2L, BigDecimal.valueOf(50), LocalDate.of(2024, 1, 1));

        FifoSelection sel1 = new FifoSelection(List.of(new FifoAllocation(s1, BigDecimal.valueOf(100))));
        FifoSelection sel2 = new FifoSelection(List.of(new FifoAllocation(s2, BigDecimal.valueOf(50))));

        given(outPlanRepository.findById("ORD0000002")).willReturn(Optional.of(plan));
        given(stockRepository.findByItemCodeAndStockStatusNotOrderByLotDateAsc(any(), any()))
                .willReturn(List.of(s1)).willReturn(List.of(s2));
        given(fifoCandidateSelector.select(List.of(s1), BigDecimal.valueOf(100))).willReturn(sel1);
        given(fifoCandidateSelector.select(List.of(s2), BigDecimal.valueOf(50))).willReturn(sel2);
        given(stockRepository.findById(1L)).willReturn(Optional.of(s1));
        given(stockRepository.findById(2L)).willReturn(Optional.of(s2));
        given(outOrderRepository.save(any(OutOrder.class))).willAnswer(i -> i.getArgument(0));

        List<OutOrder> orders = outOrderService.placeOrder("ORD0000002");

        assertThat(orders).hasSize(2);
        verify(fifoCandidateSelector, times(2)).select(any(), any());
    }

    @Test
    @DisplayName("FIFO 자동 출고지시 실패 - 존재하지 않는 출고계획")
    void placeOrder_planNotFound() {
        given(outPlanRepository.findById("NOTEXIST")).willReturn(Optional.empty());

        assertThatThrownBy(() -> outOrderService.placeOrder("NOTEXIST"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ========== placeOrderManual (수동 출고지시) ==========

    @Test
    @DisplayName("수동 출고지시 성공 - 선택 수량이 주문 수량 이하")
    void placeOrderManual_success() {
        OutPlan plan = createPlan("ORD0000003");
        plan.addDetail("ITEM001", null, "L1", BigDecimal.valueOf(100), "TON", "S001", "0001");
        OutPlanDetail detail = plan.getDetails().get(0);

        Stock stock = createStock(1L, BigDecimal.valueOf(100), LocalDate.of(2024, 1, 1));

        given(outPlanDetailRepository.findById(OutPlanDetailId.of("ORD0000003", 1)))
                .willReturn(Optional.of(detail));
        given(stockRepository.findById(1L)).willReturn(Optional.of(stock));
        given(outOrderRepository.save(any(OutOrder.class))).willAnswer(i -> i.getArgument(0));

        List<OutOrderService.StockSelection> selections = List.of(
                new OutOrderService.StockSelection(1L, BigDecimal.valueOf(100))
        );

        List<OutOrder> orders = outOrderService.placeOrderManual("ORD0000003", 1, selections);

        assertThat(orders).hasSize(1);
        assertThat(stock.getReservationStatus()).isEqualTo(ReservationStatus.RESERVED);
    }

    @Test
    @DisplayName("수동 출고지시 실패 - 선택 수량이 주문 수량 초과")
    void placeOrderManual_exceedsOrderQty() {
        OutPlan plan = createPlan("ORD0000004");
        plan.addDetail("ITEM001", null, "L1", BigDecimal.valueOf(100), "TON", "S001", "0001");
        OutPlanDetail detail = plan.getDetails().get(0);

        given(outPlanDetailRepository.findById(OutPlanDetailId.of("ORD0000004", 1)))
                .willReturn(Optional.of(detail));

        List<OutOrderService.StockSelection> selections = List.of(
                new OutOrderService.StockSelection(1L, BigDecimal.valueOf(150))
        );

        assertThatThrownBy(() -> outOrderService.placeOrderManual("ORD0000004", 1, selections))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ========== completeOrder (출고 완료) ==========

    @Test
    @DisplayName("출고 완료 처리 - 상태 COMPLETED로 전이 및 재고 수량 차감")
    void completeOrder_success() {
        Stock stock = createStock(1L, BigDecimal.valueOf(100), LocalDate.of(2024, 1, 1));
        stock.reserve();

        OutOrder order = OutOrder.create("ORD0000005", 1, 1L, BigDecimal.valueOf(60));
        order.start();

        given(outOrderRepository.findById(10L)).willReturn(Optional.of(order));
        given(stockRepository.findById(1L)).willReturn(Optional.of(stock));

        OutOrder result = outOrderService.completeOrder(10L);

        assertThat(result.getStatus()).isEqualTo(OutOrderStatus.COMPLETED);
        assertThat(stock.getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(40));
    }

    @Test
    @DisplayName("출고 완료 실패 - 존재하지 않는 출고지시")
    void completeOrder_notFound() {
        given(outOrderRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> outOrderService.completeOrder(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ========== 픽스처 ==========

    private OutPlan createPlan(String deliverOrdNo) {
        return OutPlan.create(deliverOrdNo, OutputType.DOMESTIC,
                "12가3456", "V001", "D001", "C001", "S001", "VN001");
    }

    private Stock createStock(Long id, BigDecimal qty, LocalDate lotDate) {
        Stock stock = Stock.create("STRG01", "08010101", "ITEM001", lotDate, BagType.S, "L1", qty);
        ReflectionTestUtils.setField(stock, "id", id);
        return stock;
    }
}

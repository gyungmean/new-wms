package dev.gyungmean.newwms.inventory.service;

import dev.gyungmean.newwms.inventory.domain.HoldOrder;
import dev.gyungmean.newwms.inventory.domain.Stock;
import dev.gyungmean.newwms.inventory.domain.vo.StockStatus;
import dev.gyungmean.newwms.inventory.repository.HoldOrderRepository;
import dev.gyungmean.newwms.inventory.repository.StockRepository;
import dev.gyungmean.newwms.master.domain.BagType;
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

import dev.gyungmean.newwms.common.exception.WmsStateException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private StockRepository stockRepository;
    @Mock
    private HoldOrderRepository holdOrderRepository;
    @Mock
    private FifoCandidateSelector fifoCandidateSelector;

    @InjectMocks
    private InventoryService inventoryService;

    // ========== findStock ==========

    @Test
    @DisplayName("재고 단건 조회 성공")
    void findStock_success() {
        Stock stock = createStock(BigDecimal.valueOf(100));
        given(stockRepository.findById(1L)).willReturn(Optional.of(stock));

        Stock result = inventoryService.findStock(1L);

        assertThat(result).isEqualTo(stock);
    }

    @Test
    @DisplayName("재고 단건 조회 실패 - 존재하지 않는 ID")
    void findStock_notFound() {
        given(stockRepository.findById(999L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> inventoryService.findStock(999L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ========== findAllByItemCode ==========

    @Test
    @DisplayName("품목코드로 재고 전체 조회")
    void findAllByItemCode() {
        List<Stock> stocks = List.of(createStock(BigDecimal.valueOf(10)), createStock(BigDecimal.valueOf(20)));
        given(stockRepository.findByItemCode("ITEM001")).willReturn(stocks);

        List<Stock> result = inventoryService.findAllByItemCode("ITEM001");

        assertThat(result).hasSize(2);
    }

    // ========== hold ==========

    @Test
    @DisplayName("재고 보류 - Stock.hold() 호출 및 HoldOrder 저장")
    void hold_success() {
        Stock stock = createStock(BigDecimal.valueOf(100));
        given(stockRepository.findById(1L)).willReturn(Optional.of(stock));
        given(holdOrderRepository.save(any(HoldOrder.class))).willAnswer(i -> i.getArgument(0));

        inventoryService.hold(1L, "품질 이상");

        assertThat(stock.getStockStatus()).isEqualTo(StockStatus.HOLD);
        verify(holdOrderRepository).save(any(HoldOrder.class));
    }

    @Test
    @DisplayName("재고 보류 실패 - 이미 보류 상태")
    void hold_alreadyHold() {
        Stock stock = createStock(BigDecimal.valueOf(100));
        stock.hold();
        given(stockRepository.findById(1L)).willReturn(Optional.of(stock));

        assertThatThrownBy(() -> inventoryService.hold(1L, "중복 보류"))
                .isInstanceOf(WmsStateException.class);
    }

    // ========== unhold ==========

    @Test
    @DisplayName("보류 해제 - HoldOrder 비활성화 및 Stock.unhold() 호출")
    void unhold_success() {
        Stock stock = createStock(BigDecimal.valueOf(100));
        stock.hold();
        HoldOrder holdOrder = HoldOrder.create(1L, "ITEM001", "품질 이상");

        given(stockRepository.findById(1L)).willReturn(Optional.of(stock));
        given(holdOrderRepository.findByStockIdAndActiveTrue(1L)).willReturn(Optional.of(holdOrder));

        inventoryService.unhold(1L);

        assertThat(stock.getStockStatus()).isEqualTo(StockStatus.NORMAL);
        assertThat(holdOrder.isActive()).isFalse();
    }

    @Test
    @DisplayName("보류 해제 실패 - 활성 보류 지시 없음")
    void unhold_noActiveHoldOrder() {
        Stock stock = createStock(BigDecimal.valueOf(100));
        given(stockRepository.findById(1L)).willReturn(Optional.of(stock));
        given(holdOrderRepository.findByStockIdAndActiveTrue(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> inventoryService.unhold(1L))
                .isInstanceOf(IllegalStateException.class);
    }

    // ========== adjust ==========

    @Test
    @DisplayName("재고 수량 증가")
    void adjust_increase() {
        Stock stock = createStock(BigDecimal.valueOf(100));
        given(stockRepository.findById(1L)).willReturn(Optional.of(stock));

        inventoryService.adjust(1L, BigDecimal.valueOf(50));

        assertThat(stock.getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(150));
    }

    @Test
    @DisplayName("재고 수량 감소")
    void adjust_decrease() {
        Stock stock = createStock(BigDecimal.valueOf(100));
        given(stockRepository.findById(1L)).willReturn(Optional.of(stock));

        inventoryService.adjust(1L, BigDecimal.valueOf(-30));

        assertThat(stock.getQuantity()).isEqualByComparingTo(BigDecimal.valueOf(70));
    }

    @Test
    @DisplayName("재고 수량 감소 실패 - 0 이하가 되는 경우")
    void adjust_belowZero() {
        Stock stock = createStock(BigDecimal.valueOf(10));
        given(stockRepository.findById(1L)).willReturn(Optional.of(stock));

        assertThatThrownBy(() -> inventoryService.adjust(1L, BigDecimal.valueOf(-50)))
                .isInstanceOf(WmsStateException.class);
    }

    // ========== getFifoCandidates ==========

    @Test
    @DisplayName("FIFO 후보 조회 - HOLD 제외 후 FifoCandidateSelector에 위임")
    void getFifoCandidates() {
        List<Stock> candidates = List.of(createStock(BigDecimal.valueOf(100)));
        FifoCandidateSelector.FifoSelection expected = new FifoCandidateSelector.FifoSelection(
                List.of(new FifoCandidateSelector.FifoAllocation(candidates.get(0), BigDecimal.valueOf(50)))
        );

        given(stockRepository.findByItemCodeAndStockStatusNotOrderByLotDateAsc("ITEM001", StockStatus.HOLD))
                .willReturn(candidates);
        given(fifoCandidateSelector.select(candidates, BigDecimal.valueOf(50))).willReturn(expected);

        FifoCandidateSelector.FifoSelection result = inventoryService.getFifoCandidates("ITEM001", BigDecimal.valueOf(50));

        assertThat(result).isEqualTo(expected);
        verify(fifoCandidateSelector).select(candidates, BigDecimal.valueOf(50));
    }

    private Stock createStock(BigDecimal qty) {
        return Stock.create("STRG01", "08010101", "ITEM001", LocalDate.of(2024, 1, 1), BagType.S, "L1", qty);
    }
}

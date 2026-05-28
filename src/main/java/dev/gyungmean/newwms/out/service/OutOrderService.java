package dev.gyungmean.newwms.out.service;

import dev.gyungmean.newwms.inventory.domain.Stock;
import dev.gyungmean.newwms.inventory.domain.vo.StockStatus;
import dev.gyungmean.newwms.inventory.repository.StockRepository;
import dev.gyungmean.newwms.inventory.service.FifoCandidateSelector;
import dev.gyungmean.newwms.inventory.service.FifoCandidateSelector.FifoAllocation;
import dev.gyungmean.newwms.out.domain.OutOrder;
import dev.gyungmean.newwms.out.domain.OutPlan;
import dev.gyungmean.newwms.out.domain.OutPlanDetail;
import dev.gyungmean.newwms.out.domain.OutPlanDetailId;
import dev.gyungmean.newwms.out.repository.OutOrderRepository;
import dev.gyungmean.newwms.out.repository.OutPlanDetailRepository;
import dev.gyungmean.newwms.out.repository.OutPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OutOrderService {

    private final OutPlanRepository outPlanRepository;
    private final OutPlanDetailRepository outPlanDetailRepository;
    private final OutOrderRepository outOrderRepository;
    private final StockRepository stockRepository;
    private final FifoCandidateSelector fifoCandidateSelector;

    /**
     * FIFO 자동 출고지시.
     * OutPlan의 모든 OutPlanDetail에 대해 FIFO로 재고를 선정하고 OutOrder를 생성한다.
     *
     * 구현 순서 힌트:
     * 1. deliverOrdNo로 OutPlan 조회 (없으면 IllegalArgumentException)
     * 2. plan.getDetails() 순회하며 각 detail에 대해:
     *    a. stockRepository.findByItemCodeAndStockStatusNotOrderByLotDateAsc()로 FIFO 후보 조회
     *    b. fifoCandidateSelector.select(candidates, detail.getOrderQty())로 할당
     *    c. allocation → StockSelection 변환 후 createOrders() 호출
     * 3. 생성된 전체 OutOrder 반환
     */
    @Transactional
    public List<OutOrder> placeOrder(String deliverOrdNo) {
        // TODO: 구현하세요
        throw new UnsupportedOperationException("구현 필요");
    }

    /**
     * 수동 출고지시.
     * 사용자가 직접 선택한 재고 목록으로 특정 OutPlanDetail의 OutOrder를 생성한다.
     *
     * 구현 순서 힌트:
     * 1. deliverOrdNo + deliverOrdItem으로 OutPlanDetail 조회 (없으면 IllegalArgumentException)
     * 2. selections 수량 합계가 detail.getOrderQty()를 초과하지 않는지 검증
     * 3. createOrders() 호출
     */
    @Transactional
    public List<OutOrder> placeOrderManual(String deliverOrdNo, int deliverOrdItem,
                                            List<StockSelection> selections) {
        OutPlanDetail detail = outPlanDetailRepository
            .findById(OutPlanDetailId.of(deliverOrdNo, deliverOrdItem))
            .orElseThrow(() -> new IllegalArgumentException(
                "출고계획 상세를 찾을 수 없습니다: " + deliverOrdNo + "-" + deliverOrdItem));

        BigDecimal totalQty = selections.stream()
            .map(StockSelection::qty)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalQty.compareTo(detail.getOrderQty()) > 0) {
            throw new IllegalArgumentException("선택 수량 합계가 주문 수량을 초과합니다.");
        }

        return createOrders(detail, selections);
    }

    /**
     * 출고 완료 처리.
     * OutOrder 상태를 COMPLETED로 전이하고, 연결된 Stock 수량을 차감한다.
     *
     * 구현 순서 힌트:
     * 1. outOrderId로 OutOrder 조회 (없으면 IllegalArgumentException)
     * 2. outOrder.complete() 호출
     * 3. stockId로 Stock 조회 후 수량 차감
     */
    @Transactional
    public OutOrder completeOrder(Long outOrderId) {
        throw new UnsupportedOperationException("구현 필요");
    }

    public List<OutOrder> findByOrdNo(String deliverOrdNo) {
        return outOrderRepository.findByDeliverOrdNo(deliverOrdNo);
    }

    /**
     * OutOrder 생성 공유 로직. FIFO/수동 모두 이 메서드를 거친다.
     * selections 각각에 대해 OutOrder를 생성하고 저장한다.
     *
     * 구현 순서 힌트:
     * 1. selections 순회하며 OutOrder.create() 호출
     * 2. stock.reserve() 호출로 재고 예약 상태 전이
     * 3. outOrderRepository.save()
     */
    private List<OutOrder> createOrders(OutPlanDetail detail, List<StockSelection> selections) {
        // TODO: 구현하세요
        throw new UnsupportedOperationException("구현 필요");
    }

    /**
     * FIFO 할당 결과를 수동 선택 형식으로 변환.
     * placeOrder()에서 내부적으로 사용.
     */
    private StockSelection toSelection(FifoAllocation allocation) {
        return new StockSelection(allocation.stock().getId(), allocation.allocatedQty());
    }

    // ========== 내부 타입 ==========

    /**
     * 재고 선택 단위 — FIFO 결과와 수동 선택을 동일하게 표현.
     */
    public record StockSelection(Long stockId, BigDecimal qty) {}
}

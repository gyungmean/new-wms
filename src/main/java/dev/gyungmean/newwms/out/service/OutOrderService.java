package dev.gyungmean.newwms.out.service;

import dev.gyungmean.newwms.inventory.service.FifoCandidateSelector;
import dev.gyungmean.newwms.out.domain.OutOrder;
import dev.gyungmean.newwms.out.domain.OutPlan;
import dev.gyungmean.newwms.out.domain.OutPlanDetail;
import dev.gyungmean.newwms.out.repository.OutOrderRepository;
import dev.gyungmean.newwms.out.repository.OutPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OutOrderService {

    private final OutPlanRepository outPlanRepository;
    private final OutOrderRepository outOrderRepository;
    private final FifoCandidateSelector fifoCandidateSelector;

    /**
     * 출고지시 실행.
     * OutPlan의 각 OutPlanDetail에 대해 FIFO로 Stock을 선택하고 OutOrder를 생성한다.
     *
     * 구현 순서 힌트:
     * 1. deliverOrdNo로 OutPlan 조회 (없으면 예외)
     * 2. OutPlan이 WAITING 상태인지 확인
     * 3. 각 OutPlanDetail에 대해:
     *    a. InventoryService(또는 StockRepository)로 해당 itemCode의 FIFO 후보 Stock 조회
     *    b. FifoCandidateSelector.select()로 필요 수량만큼 할당
     *    c. 각 FifoAllocation마다 OutOrder.create() 호출 후 저장
     *    d. Stock.reserve() 호출로 예약 상태 전이
     * 4. OutPlan.startLoading() 호출로 상태 전이
     */
    @Transactional
    public List<OutOrder> placeOrder(String deliverOrdNo) {
        // TODO: 구현하세요
        throw new UnsupportedOperationException("구현 필요");
    }

    /**
     * 출고 완료 처리.
     * OutOrder 상태를 COMPLETED로 전이하고, 연결된 Stock을 실제로 차감한다.
     *
     * 구현 순서 힌트:
     * 1. outOrderId로 OutOrder 조회
     * 2. OutOrder.complete() 호출
     * 3. stockId로 Stock 조회 후 수량 차감 (adjustQuantity)
     */
    @Transactional
    public OutOrder completeOrder(Long outOrderId) {
        // TODO: 구현하세요
        throw new UnsupportedOperationException("구현 필요");
    }

    public List<OutOrder> findByOrdNo(String deliverOrdNo) {
        return outOrderRepository.findByDeliverOrdNo(deliverOrdNo);
    }
}

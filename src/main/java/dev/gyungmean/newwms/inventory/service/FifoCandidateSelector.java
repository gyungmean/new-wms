package dev.gyungmean.newwms.inventory.service;

import dev.gyungmean.newwms.common.exception.ErrorCode;
import dev.gyungmean.newwms.common.exception.WmsException;
import dev.gyungmean.newwms.inventory.domain.Stock;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * FIFO 출고 후보 선택 도메인 서비스.
 * Repository에 의존하지 않는 순수 알고리즘 — lotDate ASC 정렬된 리스트를 입력으로 받음.
 */
@Component
public class FifoCandidateSelector {


    /**
     * FIFO 방식으로 요청 수량을 충족하는 재고 선택.
     *
     * @param sortedStocks lotDate ASC 정렬된 Stock 목록 (HOLD 제외된 상태)
     * @param requestedQty 출고 요청 수량
     * @return 선택 결과 (각 Stock의 사용 수량 포함)
     * @throws IllegalStateException 총 재고 수량이 요청 수량에 미달할 경우
     */
    public FifoSelection select(List<Stock> sortedStocks, BigDecimal requestedQty) {
        BigDecimal sumQty = new BigDecimal(0);
        List<FifoAllocation> allocations = new ArrayList<>();
        for(Stock stock : sortedStocks) {
            if(sumQty.compareTo(stock.getQuantity()) < 0){
                BigDecimal quantity = stock.getQuantity();
                if(requestedQty.subtract(sumQty).compareTo(quantity) < 0) {
                    quantity = requestedQty.subtract(sumQty);
                }
                allocations.add(new FifoAllocation(stock,quantity));
                sumQty = sumQty.add(stock.getQuantity());
            }
        }
        if(sumQty.compareTo(requestedQty) < 0) {
            throw new WmsException(ErrorCode.STOCK_QTY_IS_NOT_ENOUGH);
        }
        return new FifoSelection(allocations);
    }

    // ========== 결과 타입 ==========

    /**
     * FIFO 선택 결과.
     * allocations: 각 Stock에서 사용할 수량 매핑 (Stock → 사용수량)
     */
    public record FifoSelection(List<FifoAllocation> allocations) {

        /** 선택된 총 수량 */
        public BigDecimal totalAllocated() {
            return allocations.stream()
                .map(FifoAllocation::allocatedQty)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    /**
     * 단일 Stock에 대한 할당 정보.
     */
    public record FifoAllocation(Stock stock, BigDecimal allocatedQty) {}
}

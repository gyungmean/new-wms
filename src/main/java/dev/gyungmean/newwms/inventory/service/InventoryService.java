package dev.gyungmean.newwms.inventory.service;

import dev.gyungmean.newwms.inventory.domain.HoldOrder;
import dev.gyungmean.newwms.inventory.domain.Stock;
import dev.gyungmean.newwms.inventory.domain.vo.StockStatus;
import dev.gyungmean.newwms.inventory.repository.HoldOrderRepository;
import dev.gyungmean.newwms.inventory.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryService {

    private final StockRepository stockRepository;
    private final HoldOrderRepository holdOrderRepository;
    private final FifoCandidateSelector fifoCandidateSelector;

    /**
     * 재고 단건 조회.
     *
     * @throws IllegalArgumentException 재고가 없을 경우
     */
    public Stock findStock(Long stockId) {
        return stockRepository.findById(stockId)
                .orElseThrow(() -> new IllegalArgumentException("재고를 찾을 수 없습니다: " + stockId));
    }

    /**
     * 품목 코드 기준 재고 전체 조회.
     */
    public List<Stock> findAllByItemCode(String itemCode) {
        return stockRepository.findByItemCode(itemCode);
    }

    /**
     * 재고 보류 처리.
     * Stock.hold() 호출 + HoldOrder 생성 저장.
     *
     * @throws IllegalArgumentException 재고가 없을 경우
     * @throws IllegalStateException    이미 보류 상태일 경우
     */
    @Transactional
    public HoldOrder hold(Long stockId, String reason) {
        Stock stock = findStock(stockId);
        stock.hold();
        HoldOrder holdOrder = HoldOrder.create(stockId, stock.getItemCode(), reason);
        return holdOrderRepository.save(holdOrder);
    }

    /**
     * 보류 해제.
     * HoldOrder 비활성화 + Stock.unhold() 호출.
     *
     * @throws IllegalStateException 활성 보류 지시가 없을 경우
     */
    @Transactional
    public void unhold(Long stockId) {
        Stock stock = findStock(stockId);
        HoldOrder holdOrder = holdOrderRepository.findByStockIdAndActiveTrue(stockId)
                .orElseThrow(() -> new IllegalStateException("활성 보류 지시가 없습니다: " + stockId));
        holdOrder.deactivate();
        stock.unhold();
    }

    /**
     * 재고 수량 조정 (증가/감소).
     *
     * @throws IllegalStateException 감소 시 수량이 0 미만이 될 경우
     */
    @Transactional
    public void adjust(Long stockId, BigDecimal delta) {
        Stock stock = findStock(stockId);
        stock.adjustQuantity(delta);
    }

    /**
     * FIFO 출고 후보 조회.
     * HOLD 상태 재고 제외 후 lotDate ASC 정렬, FifoCandidateSelector에 위임.
     */
    public FifoCandidateSelector.FifoSelection getFifoCandidates(String itemCode, BigDecimal requestedQty) {
        List<Stock> candidates = stockRepository.findByItemCodeAndStockStatusNotOrderByLotDateAsc(itemCode, StockStatus.HOLD);
        return fifoCandidateSelector.select(candidates, requestedQty);
    }
}

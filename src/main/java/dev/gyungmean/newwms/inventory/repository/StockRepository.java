package dev.gyungmean.newwms.inventory.repository;

import dev.gyungmean.newwms.inventory.domain.Stock;
import dev.gyungmean.newwms.inventory.domain.vo.StockStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

    List<Stock> findByStorageIdAndRackNo(String storageId, String rackNo);

    List<Stock> findByItemCode(String itemCode);

    /** FIFO 정렬: lot_date ASC */
    List<Stock> findByItemCodeOrderByLotDateAsc(String itemCode);

    /** FIFO 후보: 특정 상태 제외 + lot_date ASC (HOLD 제외용) */
    List<Stock> findByItemCodeAndStockStatusNotOrderByLotDateAsc(String itemCode, StockStatus excludeStatus);
}

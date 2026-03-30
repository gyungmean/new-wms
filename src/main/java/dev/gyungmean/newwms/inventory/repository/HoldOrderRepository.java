package dev.gyungmean.newwms.inventory.repository;

import dev.gyungmean.newwms.inventory.domain.HoldOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoldOrderRepository extends JpaRepository<HoldOrder, Long> {

    List<HoldOrder> findByItemCode(String itemCode);

    List<HoldOrder> findByItemCodeAndActiveTrue(String itemCode);

    Optional<HoldOrder> findByStockIdAndActiveTrue(Long stockId);

    /** 특정 재고의 보류 이력 전체 조회 (해제된 것 포함) */
    List<HoldOrder> findByStockId(Long stockId);
}

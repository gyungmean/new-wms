package dev.gyungmean.newwms.inventory.repository;

import dev.gyungmean.newwms.inventory.domain.HoldOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HoldOrderRepository extends JpaRepository<HoldOrder, String> {

    List<HoldOrder> findByItemCode(String itemCode);

    List<HoldOrder> findByItemCodeAndActiveTrue(String itemCode);

    Optional<HoldOrder> findByStockIdAndActiveTrue(Long stockId);
}

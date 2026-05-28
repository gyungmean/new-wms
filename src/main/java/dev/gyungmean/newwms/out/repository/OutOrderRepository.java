package dev.gyungmean.newwms.out.repository;

import dev.gyungmean.newwms.out.domain.OutOrder;
import dev.gyungmean.newwms.out.domain.OutOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutOrderRepository extends JpaRepository<OutOrder, Long> {

    List<OutOrder> findByDeliverOrdNo(String deliverOrdNo);

    List<OutOrder> findByDeliverOrdNoAndDeliverOrdItem(String deliverOrdNo, Integer deliverOrdItem);

    List<OutOrder> findByStatus(OutOrderStatus status);
}

package dev.gyungmean.newwms.out.repository;

import dev.gyungmean.newwms.out.domain.IfOutPlan;
import dev.gyungmean.newwms.out.domain.IfStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IfOutPlanRepository extends JpaRepository<IfOutPlan, Long> {

    List<IfOutPlan> findByIfStatus(IfStatus ifStatus);

    boolean existsByDeliverOrdNo(String deliverOrdNo);

    Optional<IfOutPlan> findByDeliverOrdNo(String deliverOrdNo);
}

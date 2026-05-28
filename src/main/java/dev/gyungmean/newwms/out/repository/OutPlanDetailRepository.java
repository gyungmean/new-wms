package dev.gyungmean.newwms.out.repository;

import dev.gyungmean.newwms.out.domain.OutPlanDetail;
import dev.gyungmean.newwms.out.domain.OutPlanDetailId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutPlanDetailRepository extends JpaRepository<OutPlanDetail, OutPlanDetailId> {

    List<OutPlanDetail> findByIdDeliverOrdNo(String deliverOrdNo);
}

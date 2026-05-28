package dev.gyungmean.newwms.out.repository;

import dev.gyungmean.newwms.out.domain.OutPlan;
import dev.gyungmean.newwms.out.domain.OutPlanStatus;
import dev.gyungmean.newwms.out.domain.OutputType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutPlanRepository extends JpaRepository<OutPlan, String> {

    List<OutPlan> findByOrderStatus(OutPlanStatus orderStatus);

    List<OutPlan> findByOutputType(OutputType outputType);

    List<OutPlan> findByCustomerCode(String customerCode);
}

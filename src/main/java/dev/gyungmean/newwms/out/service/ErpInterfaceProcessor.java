package dev.gyungmean.newwms.out.service;

import dev.gyungmean.newwms.out.domain.IfOutPlan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ErpInterfaceProcessor {

    private final OutPlanService outPlanService;

    @Transactional
    public void processOne(IfOutPlan ifOutPlan) {
        try {
            outPlanService.createFromIf(ifOutPlan);
            ifOutPlan.markProcessed();
        } catch (Exception e) {
            ifOutPlan.markError(e.getMessage());
            log.error("IfOutPlan 처리 실패: {}", ifOutPlan.getDeliverOrdNo(), e);
        }
    }
}

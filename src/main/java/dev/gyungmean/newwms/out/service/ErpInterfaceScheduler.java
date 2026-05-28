package dev.gyungmean.newwms.out.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ErpInterfaceScheduler {

    private final IfOutPlanService ifOutPlanService;
    private final ErpInterfaceProcessor processor;

    @Scheduled(fixedDelay = 5000)
    public void poll() {
        ifOutPlanService.findPending().forEach(processor::processOne);
    }
}

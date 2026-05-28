package dev.gyungmean.newwms.out.service;

import dev.gyungmean.newwms.out.domain.IfOutPlan;
import dev.gyungmean.newwms.out.domain.IfStatus;
import dev.gyungmean.newwms.out.repository.IfOutPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IfOutPlanService {

    private final IfOutPlanRepository ifOutPlanRepository;

    public IfStatus getIfStatus(String deliverOrdNo) {
        return ifOutPlanRepository.findByDeliverOrdNo(deliverOrdNo)
                .orElseThrow(() -> new IllegalArgumentException("인터페이스 데이터 없음: " + deliverOrdNo))
                .getIfStatus();
    }

    public List<IfOutPlan> findPending() {
        return ifOutPlanRepository.findByIfStatus(IfStatus.PENDING);
    }
}

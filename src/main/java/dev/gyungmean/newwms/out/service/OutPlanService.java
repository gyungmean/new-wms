package dev.gyungmean.newwms.out.service;

import dev.gyungmean.newwms.out.domain.*;
import dev.gyungmean.newwms.out.repository.OutPlanRepository;
import dev.gyungmean.newwms.out.service.dto.OutPlanDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OutPlanService {

    private final OutPlanRepository outPlanRepository;

    @Transactional
    public void createFromIf(IfOutPlan ifOutPlan) {
        if (outPlanRepository.existsById(ifOutPlan.getDeliverOrdNo())) {
            throw new IllegalArgumentException("이미 존재하는 출고오더번호입니다: " + ifOutPlan.getDeliverOrdNo());
        }
        OutPlan plan = OutPlan.create(
                ifOutPlan.getDeliverOrdNo(),
                ifOutPlan.getOutputType(),
                ifOutPlan.getVehicleNo(),
                ifOutPlan.getVehicleCode(),
                ifOutPlan.getDriverCode(),
                ifOutPlan.getCustomerCode(),
                ifOutPlan.getSoldCode(),
                ifOutPlan.getVendorCode()
        );

        for (IfOutPlanDetail ifDetail : ifOutPlan.getDetails()) {
            plan.addDetail(ifDetail.getItemCode(), ifDetail.getBatchNo(), ifDetail.getLoadType(),
                    ifDetail.getOrderQty(), ifDetail.getItemUnit(), ifDetail.getPlant(),
                    ifDetail.getStorLoc());
        }
        outPlanRepository.save(plan);
    }

    public OutPlanDto findByOrdNo(String deliverOrdNo) {
        OutPlan plan = outPlanRepository.findById(deliverOrdNo)
                .orElseThrow(() -> new IllegalArgumentException("출고계획을 찾을 수 없습니다: " + deliverOrdNo));
        return OutPlanDto.from(plan);
    }

    public List<OutPlanDto> findByStatus(OutPlanStatus status) {
        return outPlanRepository.findByOrderStatus(status).stream()
                .map(OutPlanDto::from)
                .toList();
    }

    public List<OutPlanDto> findByOutputType(OutputType outputType) {
        return outPlanRepository.findByOutputType(outputType).stream()
                .map(OutPlanDto::from)
                .toList();
    }

    @Transactional
    public OutPlanDto startLoading(String deliverOrdNo) {
        OutPlan plan = outPlanRepository.findById(deliverOrdNo)
                .orElseThrow(() -> new IllegalArgumentException("출고계획을 찾을 수 없습니다: " + deliverOrdNo));
        plan.startLoading();
        return OutPlanDto.from(plan);
    }

    @Transactional
    public OutPlanDto confirmLoad(String deliverOrdNo) {
        OutPlan plan = outPlanRepository.findById(deliverOrdNo)
                .orElseThrow(() -> new IllegalArgumentException("출고계획을 찾을 수 없습니다: " + deliverOrdNo));
        plan.confirmLoad();
        return OutPlanDto.from(plan);
    }

    @Transactional
    public void delete(String deliverOrdNo) {
        if (!outPlanRepository.existsById(deliverOrdNo)) {
            throw new IllegalArgumentException("출고계획을 찾을 수 없습니다: " + deliverOrdNo);
        }
        outPlanRepository.deleteById(deliverOrdNo);
    }
}

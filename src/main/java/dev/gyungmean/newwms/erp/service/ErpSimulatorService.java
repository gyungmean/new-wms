package dev.gyungmean.newwms.erp.service;

import dev.gyungmean.newwms.out.controller.req.ErpOutPlanDetailReq;
import dev.gyungmean.newwms.out.controller.req.ErpOutPlanReq;
import dev.gyungmean.newwms.out.domain.IfOutPlan;
import dev.gyungmean.newwms.out.repository.IfOutPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ErpSimulatorService {

    private final IfOutPlanRepository ifOutPlanRepository;

    @Transactional
    public String sendOutPlan(ErpOutPlanReq req) {
        if (ifOutPlanRepository.existsByDeliverOrdNo(req.getDeliverOrdNo())) {
            throw new IllegalArgumentException("이미 존재하는 출고오더번호입니다: " + req.getDeliverOrdNo());
        }

        IfOutPlan ifOutPlan = IfOutPlan.create(
                req.getDeliverOrdNo(), req.getOutputType(),
                req.getVehicleNo(), req.getVehicleCode(), req.getDriverCode(),
                req.getCustomerCode(), req.getSoldCode(), req.getVendorCode()
        );

        for (ErpOutPlanDetailReq detail : req.getDetails()) {
            ifOutPlan.addDetail(
                    detail.getItemCode(), detail.getBatchNo(), detail.getLoadType(),
                    detail.getOrderQty(), detail.getItemUnit(),
                    detail.getPlant(), detail.getStorLoc()
            );
        }

        return ifOutPlanRepository.save(ifOutPlan).getDeliverOrdNo();
    }
}

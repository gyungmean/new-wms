package dev.gyungmean.newwms.out.service.dto;

import dev.gyungmean.newwms.out.domain.OutPlan;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OutPlanDto {

    private String deliverOrdNo;
    private String outputType;
    private String orderStatus;
    private String vehicleNo;
    private String vehicleCode;
    private String driverCode;
    private String customerCode;
    private String soldCode;
    private String vendorCode;
    private List<OutPlanDetailDto> details;

    public static OutPlanDto from(OutPlan plan) {
        return OutPlanDto.builder()
                .deliverOrdNo(plan.getDeliverOrdNo())
                .outputType(plan.getOutputType() != null ? plan.getOutputType().name() : null)
                .orderStatus(plan.getOrderStatus() != null ? plan.getOrderStatus().name() : null)
                .vehicleNo(plan.getVehicleNo())
                .vehicleCode(plan.getVehicleCode())
                .driverCode(plan.getDriverCode())
                .customerCode(plan.getCustomerCode())
                .soldCode(plan.getSoldCode())
                .vendorCode(plan.getVendorCode())
                .details(plan.getDetails().stream().map(OutPlanDetailDto::from).toList())
                .build();
    }
}

package dev.gyungmean.newwms.out.controller.req;

import dev.gyungmean.newwms.out.domain.OutputType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class ErpOutPlanReq {

    @NotBlank
    private String deliverOrdNo;

    @NotNull
    private OutputType outputType;

    private String vehicleNo;
    private String vehicleCode;
    private String driverCode;
    private String customerCode;
    private String soldCode;
    private String vendorCode;

    @NotEmpty
    @Valid
    private List<ErpOutPlanDetailReq> details;
}

package dev.gyungmean.newwms.out.controller.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ErpOutPlanDetailReq {

    @NotBlank
    private String itemCode;

    private String batchNo;
    private String loadType;

    @NotNull
    @Positive
    private BigDecimal orderQty;

    private String itemUnit;
    private String plant;
    private String storLoc;
}

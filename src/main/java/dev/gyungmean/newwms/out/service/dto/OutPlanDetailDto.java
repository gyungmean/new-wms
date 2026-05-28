package dev.gyungmean.newwms.out.service.dto;

import dev.gyungmean.newwms.out.domain.OutPlanDetail;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class OutPlanDetailDto {

    private String deliverOrdNo;
    private Integer deliverOrdItem;
    private String itemCode;
    private String batchNo;
    private String loadType;
    private BigDecimal orderQty;
    private BigDecimal progressQty;
    private BigDecimal completeQty;
    private String itemUnit;
    private String optionPalletCode;
    private String plant;
    private String storLoc;
    private String bookingNo;
    private boolean completed;

    public static OutPlanDetailDto from(OutPlanDetail detail) {
        return OutPlanDetailDto.builder()
                .deliverOrdNo(detail.getId().getDeliverOrdNo())
                .deliverOrdItem(detail.getId().getDeliverOrdItem())
                .itemCode(detail.getItemCode())
                .batchNo(detail.getBatchNo())
                .loadType(detail.getLoadType())
                .orderQty(detail.getOrderQty())
                .progressQty(detail.getProgressQty())
                .completeQty(detail.getCompleteQty())
                .itemUnit(detail.getItemUnit())
                .optionPalletCode(detail.getOptionPalletCode())
                .plant(detail.getPlant())
                .storLoc(detail.getStorLoc())
                .bookingNo(detail.getBookingNo())
                .completed(detail.isCompleted())
                .build();
    }
}

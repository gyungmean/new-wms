package dev.gyungmean.newwms.master.service.dto;

import dev.gyungmean.newwms.master.domain.Rack;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RackDto {

    private String rackNo;
    private String storageId;
    private String status;
    private String lugg;
    private String sidePos;
    private String sideRack;
    private Integer groupId;
    private String rackSize;
    private String zoneCode;
    private Boolean flexibleOption;
    private int cellDisplayCode;

    public static RackDto from(Rack rack) {
        return RackDto.builder()
                .rackNo(rack.getRackNo())
                .storageId(rack.getStorageId())
                .status(rack.getStatus() != null ? rack.getStatus().name() : null)
                .lugg(rack.getLugg() != null ? rack.getLugg().name() : null)
                .sidePos(rack.getSidePos() != null ? rack.getSidePos().name() : null)
                .sideRack(rack.getSideRack())
                .groupId(rack.getGroupId())
                .rackSize(rack.getRackSize() != null ? rack.getRackSize().name() : null)
                .zoneCode(rack.getZoneCode())
                .flexibleOption(rack.getFlexibleOption())
                .cellDisplayCode(rack.getCellDisplayCode())
                .build();
    }
}

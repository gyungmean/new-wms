package dev.gyungmean.newwms.master.controller.req;

import dev.gyungmean.newwms.master.domain.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RackCreateReq {

    @NotBlank
    private String rackNo;

    private String storageId;

    @NotNull
    private RackStatus status;

    @NotNull
    private LuggageStatus lugg;

    private SidePosition sidePos;

    private String sideRack;

    private Integer groupId;

    private RackSize rackSize;

    private String zoneCode;

    private Boolean flexibleOption;
}

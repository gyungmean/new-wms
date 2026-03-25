package dev.gyungmean.newwms.master.controller.req;

import dev.gyungmean.newwms.master.domain.RackStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RackSearchReq {

    private String storageId;

    private RackStatus status;

    private String zoneCode;
}

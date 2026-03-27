package dev.gyungmean.newwms.inventory.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReservationStatus {
    NONE("0", "미예약"),
    RESERVED("1", "예약");

    private final String code;
    private final String description;
}

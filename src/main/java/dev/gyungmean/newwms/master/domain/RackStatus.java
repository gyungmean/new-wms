package dev.gyungmean.newwms.master.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RackStatus {
    AVAILABLE(0, "사용가능"),
    INGRESS(1, "입고중"),
    OUTBOUND(2, "출고중"),
    RESERVED(3, "예약중"),
    DOUBLE_INGRESS(5, "이중입고"),
    EMPTY_OUTBOUND(6, "공출고"),
    UNAVAILABLE(9, "불가");

    private final int code;
    private final String description;
}

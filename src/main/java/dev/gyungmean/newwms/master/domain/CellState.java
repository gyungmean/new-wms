package dev.gyungmean.newwms.master.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CellState {
    EMPTY(0, "빈셀"),
    INGRESS(1, "입고중"),
    OUTBOUND(2, "출고중"),
    RESERVED(3, "예약중"),
    LOADED(4, "적재"),
    DOUBLE_INGRESS(5, "이중입고"),
    EMPTY_OUTBOUND(6, "공출고"),
    UNAVAILABLE(9, "불가");

    private final int displayCode;
    private final String description;

    public static CellState of(RackStatus status, LuggageStatus lugg) {
        if (status == RackStatus.AVAILABLE) {
            return lugg == LuggageStatus.LOADED ? LOADED : EMPTY;
        }
        return switch (status) {
            case INGRESS -> INGRESS;
            case OUTBOUND -> OUTBOUND;
            case RESERVED -> RESERVED;
            case DOUBLE_INGRESS -> DOUBLE_INGRESS;
            case EMPTY_OUTBOUND -> EMPTY_OUTBOUND;
            case UNAVAILABLE -> UNAVAILABLE;
            default -> throw new IllegalStateException("알 수 없는 랙 상태");
        };
    }
}

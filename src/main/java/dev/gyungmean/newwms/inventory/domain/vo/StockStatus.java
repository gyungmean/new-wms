package dev.gyungmean.newwms.inventory.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StockStatus {
    NORMAL("00", "정상"),
    HOLD("02", "보류"),
    RESERVED("06", "출고예약");

    private final String code;
    private final String description;

    public static StockStatus fromCode(String code) {
        for (StockStatus s : values()) {
            if (s.code.equals(code)) return s;
        }
        throw new IllegalArgumentException("알 수 없는 재고상태 코드: " + code);
    }
}

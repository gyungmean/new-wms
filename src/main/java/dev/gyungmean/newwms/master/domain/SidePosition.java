package dev.gyungmean.newwms.master.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SidePosition {
    OUTER(0, "외측"),
    INNER(1, "내측");

    private final int code;
    private final String description;
}

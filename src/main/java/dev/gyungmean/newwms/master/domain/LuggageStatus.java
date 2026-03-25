package dev.gyungmean.newwms.master.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LuggageStatus {
    EMPTY(0, "비적재"),
    LOADED(1, "적재");

    private final int code;
    private final String description;
}

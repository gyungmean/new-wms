package dev.gyungmean.newwms.master.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RackSize {
    H("중량"),
    M("경량");

    private final String description;
}

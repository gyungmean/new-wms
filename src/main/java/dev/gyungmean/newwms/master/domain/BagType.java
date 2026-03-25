package dev.gyungmean.newwms.master.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BagType {
    S("SmallBag"),
    B("BigBag"),
    T("Tank");

    private final String description;
}

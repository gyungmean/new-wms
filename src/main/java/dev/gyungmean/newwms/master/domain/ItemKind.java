package dev.gyungmean.newwms.master.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemKind {
    ERP("ERP"),
    WMS("WMS");

    private final String description;
}

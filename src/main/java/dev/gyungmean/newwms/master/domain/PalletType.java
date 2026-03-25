package dev.gyungmean.newwms.master.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PalletType {
    D("Domestic"),
    E("Export");

    private final String description;
}

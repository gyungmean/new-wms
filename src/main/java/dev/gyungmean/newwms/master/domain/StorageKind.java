package dev.gyungmean.newwms.master.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StorageKind {
    A("자동창고"),
    M("수동창고");

    private final String description;
}

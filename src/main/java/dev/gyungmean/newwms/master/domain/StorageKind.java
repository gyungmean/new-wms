package dev.gyungmean.newwms.master.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StorageKind {
    A("자동창고"),
    M("수동창고"),
    W("대기장");

    private final String description;
}

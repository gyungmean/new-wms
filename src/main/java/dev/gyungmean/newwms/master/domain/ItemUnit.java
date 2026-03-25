package dev.gyungmean.newwms.master.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemUnit {
    TON("Ton"),
    EA("Each");

    private final String description;
}

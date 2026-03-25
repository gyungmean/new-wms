package dev.gyungmean.newwms.master.service.dto;

import dev.gyungmean.newwms.master.domain.Item;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ItemDto {

    private String itemCode;
    private String itemName;
    private String itemKind;
    private String zoneCode;
    private String itemUnit;
    private String itemSize;
    private BigDecimal weightUnit;
    private String bagType;

    public static ItemDto from(Item item) {
        return ItemDto.builder()
                .itemCode(item.getItemCode())
                .itemName(item.getItemName())
                .itemKind(item.getItemKind() != null ? item.getItemKind().name() : null)
                .zoneCode(item.getZoneCode())
                .itemUnit(item.getItemUnit() != null ? item.getItemUnit().name() : null)
                .itemSize(item.getItemSize() != null ? item.getItemSize().name() : null)
                .weightUnit(item.getWeightUnit())
                .bagType(item.getBagType() != null ? item.getBagType().name() : null)
                .build();
    }
}

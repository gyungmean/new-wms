package dev.gyungmean.newwms.master.domain;

import dev.gyungmean.newwms.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;

@Entity
@Table(name = "w_itm_i")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "item_code", length = 18)
    private String itemCode;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_kind", nullable = false)
    private ItemKind itemKind;

    @Column(name = "zone_code", length = 2)
    private String zoneCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_unit", length = 3, nullable = false)
    private ItemUnit itemUnit;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_size", length = 1)
    private ItemSize itemSize;

    @Column(name = "weight_unit")
    private BigDecimal weightUnit;

    @Enumerated(EnumType.STRING)
    @Column(name = "bag_type", length = 1)
    private BagType bagType;

    @Builder
    public Item(String itemCode, String itemName, ItemKind itemKind,
                String zoneCode, ItemUnit itemUnit, ItemSize itemSize,
                BigDecimal weightUnit, BagType bagType) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemKind = itemKind;
        this.zoneCode = zoneCode;
        this.itemUnit = itemUnit;
        this.itemSize = itemSize;
        this.weightUnit = weightUnit;
        this.bagType = bagType;
    }

    public void update(String itemName, ItemKind itemKind, String zoneCode,
                       ItemUnit itemUnit, ItemSize itemSize,
                       BigDecimal weightUnit, BagType bagType) {
        this.itemName = itemName;
        this.itemKind = itemKind;
        this.zoneCode = zoneCode;
        this.itemUnit = itemUnit;
        this.itemSize = itemSize;
        this.weightUnit = weightUnit;
        this.bagType = bagType;
    }

    @Override
    public String getId() {
        return itemCode;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}

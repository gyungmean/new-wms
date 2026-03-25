package dev.gyungmean.newwms.master.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ItemTest {

    @Test
    @DisplayName("Item 엔티티 생성")
    void createItem() {
        Item item = Item.builder()
                .itemCode("HK100PP25DW")
                .itemName("테스트 품목")
                .itemKind(ItemKind.ERP)
                .zoneCode("01")
                .itemUnit(ItemUnit.TON)
                .itemSize(ItemSize.H)
                .weightUnit(new BigDecimal("0.025"))
                .bagType(BagType.S)
                .build();

        assertThat(item.getItemCode()).isEqualTo("HK100PP25DW");
        assertThat(item.getItemName()).isEqualTo("테스트 품목");
        assertThat(item.getItemKind()).isEqualTo(ItemKind.ERP);
        assertThat(item.getZoneCode()).isEqualTo("01");
        assertThat(item.getItemUnit()).isEqualTo(ItemUnit.TON);
        assertThat(item.getItemSize()).isEqualTo(ItemSize.H);
        assertThat(item.getWeightUnit()).isEqualByComparingTo(new BigDecimal("0.025"));
        assertThat(item.getBagType()).isEqualTo(BagType.S);
    }

    @Test
    @DisplayName("Item update 메서드")
    void updateItem() {
        Item item = Item.builder()
                .itemCode("HK100PP25DW")
                .itemName("원래 이름")
                .itemKind(ItemKind.ERP)
                .itemUnit(ItemUnit.TON)
                .build();

        item.update("변경된 이름", ItemKind.WMS, "02",
                ItemUnit.EA, ItemSize.M, new BigDecimal("1.0"), BagType.B);

        assertThat(item.getItemName()).isEqualTo("변경된 이름");
        assertThat(item.getItemKind()).isEqualTo(ItemKind.WMS);
        assertThat(item.getZoneCode()).isEqualTo("02");
        assertThat(item.getItemUnit()).isEqualTo(ItemUnit.EA);
        assertThat(item.getItemSize()).isEqualTo(ItemSize.M);
        assertThat(item.getWeightUnit()).isEqualByComparingTo(new BigDecimal("1.0"));
        assertThat(item.getBagType()).isEqualTo(BagType.B);
    }

    @Test
    @DisplayName("Item isNew - createdAt이 null이면 true")
    void isNewWhenCreatedAtNull() {
        Item item = Item.builder()
                .itemCode("TEST001")
                .itemName("테스트")
                .itemKind(ItemKind.ERP)
                .itemUnit(ItemUnit.TON)
                .build();

        assertThat(item.isNew()).isTrue();
    }

    @Test
    @DisplayName("BagType enum 값 확인")
    void bagTypeValues() {
        assertThat(BagType.S.getDescription()).isEqualTo("SmallBag");
        assertThat(BagType.B.getDescription()).isEqualTo("BigBag");
        assertThat(BagType.T.getDescription()).isEqualTo("Tank");
    }
}

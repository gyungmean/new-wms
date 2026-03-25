package dev.gyungmean.newwms.master.repository;

import dev.gyungmean.newwms.master.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("Item 저장 및 조회")
    void saveAndFind() {
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

        itemRepository.save(item);

        Optional<Item> found = itemRepository.findById("HK100PP25DW");
        assertThat(found).isPresent();
        assertThat(found.get().getItemName()).isEqualTo("테스트 품목");
        assertThat(found.get().getItemKind()).isEqualTo(ItemKind.ERP);
    }

    @Test
    @DisplayName("ItemKind로 조회")
    void findByItemKind() {
        itemRepository.save(createItem("ITEM001", "ERP품목", ItemKind.ERP));
        itemRepository.save(createItem("ITEM002", "WMS품목", ItemKind.WMS));

        List<Item> erpItems = itemRepository.findByItemKind(ItemKind.ERP);
        assertThat(erpItems).hasSize(1);
        assertThat(erpItems.get(0).getItemCode()).isEqualTo("ITEM001");
    }

    @Test
    @DisplayName("BagType으로 조회")
    void findByBagType() {
        itemRepository.save(createItemWithBagType("ITEM001", BagType.S));
        itemRepository.save(createItemWithBagType("ITEM002", BagType.B));
        itemRepository.save(createItemWithBagType("ITEM003", BagType.S));

        List<Item> smallBagItems = itemRepository.findByBagType(BagType.S);
        assertThat(smallBagItems).hasSize(2);
    }

    @Test
    @DisplayName("품목명 키워드 검색")
    void findByItemNameContaining() {
        itemRepository.save(createItem("ITEM001", "폴리에틸렌 25kg", ItemKind.ERP));
        itemRepository.save(createItem("ITEM002", "폴리프로필렌 500kg", ItemKind.ERP));
        itemRepository.save(createItem("ITEM003", "첨가제 A", ItemKind.WMS));

        List<Item> result = itemRepository.findByItemNameContaining("폴리");
        assertThat(result).hasSize(2);
    }

    private Item createItem(String code, String name, ItemKind kind) {
        return Item.builder()
                .itemCode(code)
                .itemName(name)
                .itemKind(kind)
                .itemUnit(ItemUnit.TON)
                .build();
    }

    private Item createItemWithBagType(String code, BagType bagType) {
        return Item.builder()
                .itemCode(code)
                .itemName("품목 " + code)
                .itemKind(ItemKind.ERP)
                .itemUnit(ItemUnit.TON)
                .bagType(bagType)
                .build();
    }
}

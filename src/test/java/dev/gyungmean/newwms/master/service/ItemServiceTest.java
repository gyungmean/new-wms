package dev.gyungmean.newwms.master.service;

import dev.gyungmean.newwms.master.controller.req.ItemCreateReq;
import dev.gyungmean.newwms.master.domain.*;
import dev.gyungmean.newwms.master.repository.ItemRepository;
import dev.gyungmean.newwms.master.service.dto.ItemDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    @DisplayName("품목 조회 성공")
    void findByCode_success() {
        Item item = Item.builder()
                .itemCode("HK100PP25DW")
                .itemName("테스트 품목")
                .itemKind(ItemKind.ERP)
                .itemUnit(ItemUnit.TON)
                .bagType(BagType.S)
                .build();

        given(itemRepository.findById("HK100PP25DW")).willReturn(Optional.of(item));

        ItemDto result = itemService.findByCode("HK100PP25DW");

        assertThat(result.getItemCode()).isEqualTo("HK100PP25DW");
        assertThat(result.getItemName()).isEqualTo("테스트 품목");
        assertThat(result.getBagType()).isEqualTo("S");
    }

    @Test
    @DisplayName("품목 조회 실패 - 존재하지 않는 코드")
    void findByCode_notFound() {
        given(itemRepository.findById("NOTEXIST")).willReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.findByCode("NOTEXIST"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("품목을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("품목 생성 성공")
    void create_success() {
        ItemCreateReq req = createReq();

        given(itemRepository.existsById("NEWITEM01")).willReturn(false);
        given(itemRepository.save(any(Item.class))).willAnswer(invocation -> invocation.getArgument(0));

        ItemDto result = itemService.create(req);

        assertThat(result.getItemCode()).isEqualTo("NEWITEM01");
        verify(itemRepository).save(any(Item.class));
    }

    @Test
    @DisplayName("품목 생성 실패 - 중복 코드")
    void create_duplicate() {
        ItemCreateReq req = createReq();

        given(itemRepository.existsById("NEWITEM01")).willReturn(true);

        assertThatThrownBy(() -> itemService.create(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는 품목코드");
    }

    @Test
    @DisplayName("품목 삭제 성공")
    void delete_success() {
        given(itemRepository.existsById("HK100PP25DW")).willReturn(true);

        itemService.delete("HK100PP25DW");

        verify(itemRepository).deleteById("HK100PP25DW");
    }

    @Test
    @DisplayName("품목 삭제 실패 - 존재하지 않는 코드")
    void delete_notFound() {
        given(itemRepository.existsById("NOTEXIST")).willReturn(false);

        assertThatThrownBy(() -> itemService.delete("NOTEXIST"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private ItemCreateReq createReq() {
        try {
            ItemCreateReq req = new ItemCreateReq();
            var itemCodeField = ItemCreateReq.class.getDeclaredField("itemCode");
            itemCodeField.setAccessible(true);
            itemCodeField.set(req, "NEWITEM01");

            var itemNameField = ItemCreateReq.class.getDeclaredField("itemName");
            itemNameField.setAccessible(true);
            itemNameField.set(req, "신규 품목");

            var itemKindField = ItemCreateReq.class.getDeclaredField("itemKind");
            itemKindField.setAccessible(true);
            itemKindField.set(req, ItemKind.ERP);

            var itemUnitField = ItemCreateReq.class.getDeclaredField("itemUnit");
            itemUnitField.setAccessible(true);
            itemUnitField.set(req, ItemUnit.TON);

            return req;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

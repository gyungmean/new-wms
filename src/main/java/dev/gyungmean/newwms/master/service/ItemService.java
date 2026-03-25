package dev.gyungmean.newwms.master.service;

import dev.gyungmean.newwms.master.controller.req.ItemCreateReq;
import dev.gyungmean.newwms.master.controller.req.ItemSearchReq;
import dev.gyungmean.newwms.master.domain.Item;
import dev.gyungmean.newwms.master.repository.ItemRepository;
import dev.gyungmean.newwms.master.service.dto.ItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public ItemDto create(ItemCreateReq req) {
        if (itemRepository.existsById(req.getItemCode())) {
            throw new IllegalArgumentException("이미 존재하는 품목코드입니다: " + req.getItemCode());
        }

        Item item = Item.builder()
                .itemCode(req.getItemCode())
                .itemName(req.getItemName())
                .itemKind(req.getItemKind())
                .zoneCode(req.getZoneCode())
                .itemUnit(req.getItemUnit())
                .itemSize(req.getItemSize())
                .weightUnit(req.getWeightUnit())
                .bagType(req.getBagType())
                .build();

        return ItemDto.from(itemRepository.save(item));
    }

    public ItemDto findByCode(String itemCode) {
        Item item = itemRepository.findById(itemCode)
                .orElseThrow(() -> new IllegalArgumentException("품목을 찾을 수 없습니다: " + itemCode));
        return ItemDto.from(item);
    }

    public List<ItemDto> search(ItemSearchReq req) {
        List<Item> items;

        if (req.getItemName() != null && !req.getItemName().isBlank()) {
            items = itemRepository.findByItemNameContaining(req.getItemName());
        } else if (req.getItemKind() != null) {
            items = itemRepository.findByItemKind(req.getItemKind());
        } else if (req.getBagType() != null) {
            items = itemRepository.findByBagType(req.getBagType());
        } else {
            items = itemRepository.findAll();
        }

        return items.stream().map(ItemDto::from).toList();
    }

    @Transactional
    public ItemDto update(String itemCode, ItemCreateReq req) {
        Item item = itemRepository.findById(itemCode)
                .orElseThrow(() -> new IllegalArgumentException("품목을 찾을 수 없습니다: " + itemCode));

        item.update(
                req.getItemName(),
                req.getItemKind(),
                req.getZoneCode(),
                req.getItemUnit(),
                req.getItemSize(),
                req.getWeightUnit(),
                req.getBagType()
        );

        return ItemDto.from(item);
    }

    @Transactional
    public void delete(String itemCode) {
        if (!itemRepository.existsById(itemCode)) {
            throw new IllegalArgumentException("품목을 찾을 수 없습니다: " + itemCode);
        }
        itemRepository.deleteById(itemCode);
    }
}

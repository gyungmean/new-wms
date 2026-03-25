package dev.gyungmean.newwms.master.controller;

import dev.gyungmean.newwms.common.api.ApiResponse;
import dev.gyungmean.newwms.master.controller.req.ItemCreateReq;
import dev.gyungmean.newwms.master.controller.req.ItemSearchReq;
import dev.gyungmean.newwms.master.service.ItemService;
import dev.gyungmean.newwms.master.service.dto.ItemDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ApiResponse<ItemDto> create(@Valid @RequestBody ItemCreateReq req) {
        return ApiResponse.ok(itemService.create(req));
    }

    @GetMapping("/{itemCode}")
    public ApiResponse<ItemDto> findByCode(@PathVariable String itemCode) {
        return ApiResponse.ok(itemService.findByCode(itemCode));
    }

    @GetMapping
    public ApiResponse<List<ItemDto>> search(@ModelAttribute ItemSearchReq req) {
        return ApiResponse.ok(itemService.search(req));
    }

    @PutMapping("/{itemCode}")
    public ApiResponse<ItemDto> update(@PathVariable String itemCode,
                                       @Valid @RequestBody ItemCreateReq req) {
        return ApiResponse.ok(itemService.update(itemCode, req));
    }

    @DeleteMapping("/{itemCode}")
    public ApiResponse<Void> delete(@PathVariable String itemCode) {
        itemService.delete(itemCode);
        return ApiResponse.ok(null);
    }
}

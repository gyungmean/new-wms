package dev.gyungmean.newwms.master.controller;

import dev.gyungmean.newwms.common.api.ApiResponse;
import dev.gyungmean.newwms.master.controller.req.StorageCreateReq;
import dev.gyungmean.newwms.master.service.StorageService;
import dev.gyungmean.newwms.master.service.dto.StorageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/storages")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @PostMapping
    public ApiResponse<StorageDto> create(@Valid @RequestBody StorageCreateReq req) {
        return ApiResponse.ok(storageService.create(req));
    }

    @GetMapping("/{storageId}")
    public ApiResponse<StorageDto> findById(@PathVariable String storageId) {
        return ApiResponse.ok(storageService.findById(storageId));
    }

    @GetMapping
    public ApiResponse<List<StorageDto>> findAll() {
        return ApiResponse.ok(storageService.findAll());
    }

    @PutMapping("/{storageId}")
    public ApiResponse<StorageDto> update(@PathVariable String storageId,
                                          @Valid @RequestBody StorageCreateReq req) {
        return ApiResponse.ok(storageService.update(storageId, req));
    }

    @DeleteMapping("/{storageId}")
    public ApiResponse<Void> delete(@PathVariable String storageId) {
        storageService.delete(storageId);
        return ApiResponse.ok(null);
    }
}

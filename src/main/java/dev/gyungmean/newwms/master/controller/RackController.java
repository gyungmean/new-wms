package dev.gyungmean.newwms.master.controller;

import dev.gyungmean.newwms.common.api.ApiResponse;
import dev.gyungmean.newwms.master.controller.req.RackCreateReq;
import dev.gyungmean.newwms.master.controller.req.RackSearchReq;
import dev.gyungmean.newwms.master.service.RackService;
import dev.gyungmean.newwms.master.service.dto.RackDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/master/racks")
@RequiredArgsConstructor
public class RackController {

    private final RackService rackService;

    @PostMapping
    public ApiResponse<RackDto> create(@Valid @RequestBody RackCreateReq req) {
        return ApiResponse.ok(rackService.create(req));
    }

    @GetMapping("/{storageId}/{rackNo}")
    public ApiResponse<RackDto> findByRackNo(@PathVariable String storageId,
                                              @PathVariable String rackNo) {
        return ApiResponse.ok(rackService.findByRackNo(storageId, rackNo));
    }

    @GetMapping
    public ApiResponse<List<RackDto>> search(@ModelAttribute RackSearchReq req) {
        return ApiResponse.ok(rackService.search(req));
    }

    @DeleteMapping("/{storageId}/{rackNo}")
    public ApiResponse<Void> delete(@PathVariable String storageId,
                                    @PathVariable String rackNo) {
        rackService.delete(storageId, rackNo);
        return ApiResponse.ok(null);
    }

    @GetMapping("/{storageId}/{rackNo}/partner")
    public ApiResponse<RackDto> findPartner(@PathVariable String storageId,
                                             @PathVariable String rackNo) {
        return ApiResponse.ok(rackService.findPartner(storageId, rackNo));
    }
}

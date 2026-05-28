package dev.gyungmean.newwms.out.controller;

import dev.gyungmean.newwms.common.api.ApiResponse;
import dev.gyungmean.newwms.out.domain.OutPlanStatus;
import dev.gyungmean.newwms.out.domain.OutputType;
import dev.gyungmean.newwms.out.service.OutPlanService;
import dev.gyungmean.newwms.out.service.dto.OutPlanDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/out/plans")
@RequiredArgsConstructor
public class OutPlanController {

    private final OutPlanService outPlanService;

    @GetMapping("/{deliverOrdNo}")
    public ApiResponse<OutPlanDto> findByOrdNo(@PathVariable String deliverOrdNo) {
        return ApiResponse.ok(outPlanService.findByOrdNo(deliverOrdNo));
    }

    @GetMapping
    public ApiResponse<List<OutPlanDto>> search(
            @RequestParam(required = false) OutPlanStatus status,
            @RequestParam(required = false) OutputType outputType) {
        if (status != null) {
            return ApiResponse.ok(outPlanService.findByStatus(status));
        }
        if (outputType != null) {
            return ApiResponse.ok(outPlanService.findByOutputType(outputType));
        }
        return ApiResponse.ok(outPlanService.findByStatus(OutPlanStatus.WAITING));
    }

    @PatchMapping("/{deliverOrdNo}/start-loading")
    public ApiResponse<OutPlanDto> startLoading(@PathVariable String deliverOrdNo) {
        return ApiResponse.ok(outPlanService.startLoading(deliverOrdNo));
    }

    @PatchMapping("/{deliverOrdNo}/confirm-load")
    public ApiResponse<OutPlanDto> confirmLoad(@PathVariable String deliverOrdNo) {
        return ApiResponse.ok(outPlanService.confirmLoad(deliverOrdNo));
    }

    @DeleteMapping("/{deliverOrdNo}")
    public ApiResponse<Void> delete(@PathVariable String deliverOrdNo) {
        outPlanService.delete(deliverOrdNo);
        return ApiResponse.ok(null);
    }
}

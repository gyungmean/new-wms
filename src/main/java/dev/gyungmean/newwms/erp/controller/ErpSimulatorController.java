package dev.gyungmean.newwms.erp.controller;

import dev.gyungmean.newwms.common.api.ApiResponse;
import dev.gyungmean.newwms.erp.service.ErpSimulatorService;
import dev.gyungmean.newwms.out.controller.req.ErpOutPlanReq;
import dev.gyungmean.newwms.out.domain.IfStatus;
import dev.gyungmean.newwms.out.service.IfOutPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * ERP 시뮬레이터 컨트롤러.
 * 실제 SAP ERP가 WMS 스테이징 테이블에 데이터를 INSERT하는 행위를 흉내낸다.
 * 테스트/개발 목적으로만 사용.
 */
@RestController
@RequestMapping("/api/erp-simulator")
@RequiredArgsConstructor
public class ErpSimulatorController {

    private final ErpSimulatorService erpSimulatorService;
    private final IfOutPlanService ifOutPlanService;

    @PostMapping("/outplan")
    public ApiResponse<String> sendOutPlan(@Valid @RequestBody ErpOutPlanReq req) {
        return ApiResponse.ok(erpSimulatorService.sendOutPlan(req));
    }

    @GetMapping("/outplan/{deliverOrdNo}/status")
    public ApiResponse<IfStatus> getIfStatus(@PathVariable String deliverOrdNo) {
        return ApiResponse.ok(ifOutPlanService.getIfStatus(deliverOrdNo));
    }
}

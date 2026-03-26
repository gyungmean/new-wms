package dev.gyungmean.newwms.master.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RackTest {

    // ========== 생성 ==========

    @Test
    @DisplayName("Rack 생성 - 자동 파생 필드 확인 (sidePos, sideRack, groupId, rackSize)")
    void createRack_derivedFields() {
        Rack rack = createRack("08010301", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        assertThat(rack.getRackNo()).isEqualTo("08010301");
        assertThat(rack.getStorageId()).isEqualTo("08010301");
        assertThat(rack.getRackNo()).isEqualTo("08010301");
        assertThat(rack.getRackNo()).isEqualTo("08010301");
        assertThat(rack.getRackNo()).isEqualTo("08010301");
        assertThat(rack.getRackNo()).isEqualTo("08010301");
        assertThat(rack.getRackNo()).isEqualTo("08010301");
        assertThat(rack.getRackNo()).isEqualTo("08010301");
        assertThat(rack.getRackNo()).isEqualTo("08010301");

        // TODO: rackNo, storageId, status, lugg 기본 필드 검증
        // TODO: sidePos가 INNER인지 (z=1)
        // TODO: sideRack이 "08020301"인지 (z 뒤집힘)
        // TODO: groupId가 자동 계산되었는지 (null이 아님)
        // TODO: rackSize가 H인지 (y=1)
    }

    // ========== isAvailable ==========

    @Test
    @DisplayName("isAvailable - AVAILABLE + EMPTY이면 true")
    void isAvailable_true() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        // TODO: isAvailable()이 true인지 검증
    }

    @Test
    @DisplayName("isAvailable - AVAILABLE + LOADED이면 false")
    void isAvailable_loadedIsFalse() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.LOADED);

        // TODO: isAvailable()이 false인지 검증
    }

    @Test
    @DisplayName("isAvailable - INGRESS 상태이면 false")
    void isAvailable_ingressIsFalse() {
        Rack rack = createRack("01010101", RackStatus.INGRESS, LuggageStatus.EMPTY);

        // TODO: isAvailable()이 false인지 검증
    }

    // ========== isDoubleDeepInner ==========

    @Test
    @DisplayName("isDoubleDeepInner - z=1이면 true")
    void isDoubleDeepInner_z1() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        // TODO: isDoubleDeepInner()가 true인지 검증
    }

    @Test
    @DisplayName("isDoubleDeepInner - z=2이면 false")
    void isDoubleDeepInner_z2() {
        Rack rack = createRack("01020101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        // TODO: isDoubleDeepInner()가 false인지 검증
    }

    // ========== 상태 전이: 입고 ==========

    @Test
    @DisplayName("startIngress - 사용 가능한 랙이면 INGRESS로 변경")
    void startIngress_success() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        rack.startIngress();

        // TODO: status가 INGRESS인지 검증
    }

    @Test
    @DisplayName("startIngress - 사용 불가능한 랙이면 예외")
    void startIngress_fail() {
        Rack rack = createRack("01010101", RackStatus.INGRESS, LuggageStatus.EMPTY);

        // TODO: startIngress() 호출 시 IllegalStateException 검증
    }

    @Test
    @DisplayName("completeIngress - INGRESS 상태면 AVAILABLE+LOADED")
    void completeIngress_success() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);
        rack.startIngress();  // 먼저 입고 시작

        rack.completeIngress();

        // TODO: status가 AVAILABLE, lugg가 LOADED인지 검증
    }

    @Test
    @DisplayName("completeIngress - INGRESS가 아니면 예외")
    void completeIngress_fail() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        // TODO: completeIngress() 호출 시 IllegalStateException 검증
    }

    // ========== 상태 전이: 출고 ==========

    @Test
    @DisplayName("startOutbound - 사용 가능한 랙이면 OUTBOUND로 변경")
    void startOutbound_success() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        rack.startOutbound();

        // TODO: status가 OUTBOUND인지 검증
    }

    @Test
    @DisplayName("startOutbound - 사용 불가능한 랙이면 예외")
    void startOutbound_fail() {
        Rack rack = createRack("01010101", RackStatus.OUTBOUND, LuggageStatus.EMPTY);

        // TODO: startOutbound() 호출 시 IllegalStateException 검증
    }

    @Test
    @DisplayName("completeOutbound - OUTBOUND 상태면 AVAILABLE+EMPTY")
    void completeOutbound_success() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);
        rack.startOutbound();  // 먼저 출고 시작

        rack.completeOutbound();

        // TODO: status가 AVAILABLE, lugg가 EMPTY인지 검증
    }

    @Test
    @DisplayName("completeOutbound - OUTBOUND가 아니면 예외")
    void completeOutbound_fail() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        // TODO: completeOutbound() 호출 시 IllegalStateException 검증
    }

    // ========== getCellState / getCellDisplayCode ==========

    @Test
    @DisplayName("getCellState - AVAILABLE+EMPTY → EMPTY(0)")
    void cellState_empty() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        // TODO: getCellState()가 CellState.EMPTY인지 검증
        // TODO: getCellDisplayCode()가 0인지 검증
    }

    @Test
    @DisplayName("getCellState - AVAILABLE+LOADED → LOADED(4)")
    void cellState_loaded() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.LOADED);

        // TODO: getCellState()가 CellState.LOADED인지 검증
        // TODO: getCellDisplayCode()가 4인지 검증
    }

    @Test
    @DisplayName("getCellState - INGRESS → INGRESS(1)")
    void cellState_ingress() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);
        rack.startIngress();

        // TODO: getCellState()가 CellState.INGRESS인지 검증
        // TODO: getCellDisplayCode()가 1인지 검증
    }

    @Test
    @DisplayName("getCellState - UNAVAILABLE → UNAVAILABLE(9)")
    void cellState_unavailable() {
        Rack rack = createRack("01010101", RackStatus.UNAVAILABLE, LuggageStatus.EMPTY);

        // TODO: getCellState()가 CellState.UNAVAILABLE인지 검증
        // TODO: getCellDisplayCode()가 9인지 검증
    }

    // ========== Persistable ==========

    @Test
    @DisplayName("isNew - createdAt이 null이면 true")
    void isNew() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        // TODO: isNew()가 true인지 검증
    }

    @Test
    @DisplayName("getId - rackNo 반환")
    void getId() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        // TODO: getId()가 "01010101"인지 검증
    }

    // ========== 헬퍼 메서드 ==========

    private Rack createRack(String rackNo, RackStatus status, LuggageStatus lugg) {
        return Rack.builder().rackNo(rackNo).storageId("STRG000001").status(status).lugg(lugg)
            .zoneCode("01").build();
    }
}

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
        assertThat(rack.getStorageId()).isEqualTo("STRG000001");
        assertThat(rack.getStatus()).isEqualTo(RackStatus.AVAILABLE);
        assertThat(rack.getLugg()).isEqualTo(LuggageStatus.EMPTY);
        assertThat(rack.getSidePos()).isEqualTo(SidePosition.INNER);
        assertThat(rack.getSideRack()).isEqualTo("08020301");
        assertThat(rack.getGroupId()).isNotNull();
        assertThat(rack.getRackSize()).isEqualTo(RackSize.H);
    }

    // ========== isAvailable ==========

    @Test
    @DisplayName("isAvailable - AVAILABLE + EMPTY이면 true")
    void isAvailable_true() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        assertThat(rack.isAvailable()).isTrue();
    }

    @Test
    @DisplayName("isAvailable - AVAILABLE + LOADED이면 false")
    void isAvailable_loadedIsFalse() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.LOADED);

        assertThat(rack.isAvailable()).isFalse();
    }

    @Test
    @DisplayName("isAvailable - INGRESS 상태이면 false")
    void isAvailable_ingressIsFalse() {
        Rack rack = createRack("01010101", RackStatus.INGRESS, LuggageStatus.EMPTY);

        assertThat(rack.isAvailable()).isFalse();
    }

    // ========== isDoubleDeepInner ==========

    @Test
    @DisplayName("isDoubleDeepInner - z=1이면 true")
    void isDoubleDeepInner_z1() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        assertThat(rack.isDoubleDeepInner()).isTrue();
    }

    @Test
    @DisplayName("isDoubleDeepInner - z=2이면 false")
    void isDoubleDeepInner_z2() {
        Rack rack = createRack("01020101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        assertThat(rack.isDoubleDeepInner()).isFalse();
    }

    // ========== 상태 전이: 입고 ==========

    @Test
    @DisplayName("startIngress - 사용 가능한 랙이면 INGRESS로 변경")
    void startIngress_success() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        rack.startIngress();

        assertThat(rack.getStatus()).isEqualTo(RackStatus.INGRESS);
    }

    @Test
    @DisplayName("startIngress - 사용 불가능한 랙이면 예외")
    void startIngress_fail() {
        Rack rack = createRack("01010101", RackStatus.INGRESS, LuggageStatus.EMPTY);

        assertThatThrownBy(rack::startIngress)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("completeIngress - INGRESS 상태면 AVAILABLE+LOADED")
    void completeIngress_success() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);
        rack.startIngress();  // 먼저 입고 시작

        rack.completeIngress();

        assertThat(rack.getStatus()).isEqualTo(RackStatus.AVAILABLE);
        assertThat(rack.getLugg()).isEqualTo(LuggageStatus.LOADED);
    }

    @Test
    @DisplayName("completeIngress - INGRESS가 아니면 예외")
    void completeIngress_fail() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        assertThatThrownBy(rack::completeIngress)
                .isInstanceOf(IllegalStateException.class);
    }

    // ========== 상태 전이: 출고 ==========

    @Test
    @DisplayName("startOutbound - 사용 가능한 랙이면 OUTBOUND로 변경")
    void startOutbound_success() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        rack.startOutbound();

        assertThat(rack.getStatus()).isEqualTo(RackStatus.OUTBOUND);
    }

    @Test
    @DisplayName("startOutbound - 사용 불가능한 랙이면 예외")
    void startOutbound_fail() {
        Rack rack = createRack("01010101", RackStatus.OUTBOUND, LuggageStatus.EMPTY);

        assertThatThrownBy(rack::startOutbound)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("completeOutbound - OUTBOUND 상태면 AVAILABLE+EMPTY")
    void completeOutbound_success() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);
        rack.startOutbound();  // 먼저 출고 시작

        rack.completeOutbound();

        assertThat(rack.getStatus()).isEqualTo(RackStatus.AVAILABLE);
        assertThat(rack.getLugg()).isEqualTo(LuggageStatus.EMPTY);
    }

    @Test
    @DisplayName("completeOutbound - OUTBOUND가 아니면 예외")
    void completeOutbound_fail() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        assertThatThrownBy(rack::completeOutbound)
                .isInstanceOf(IllegalStateException.class);
    }

    // ========== getCellState / getCellDisplayCode ==========

    @Test
    @DisplayName("getCellState - AVAILABLE+EMPTY → EMPTY(0)")
    void cellState_empty() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        assertThat(rack.getCellState()).isEqualTo(CellState.EMPTY);
        assertThat(rack.getCellDisplayCode()).isEqualTo(0);
    }

    @Test
    @DisplayName("getCellState - AVAILABLE+LOADED → LOADED(4)")
    void cellState_loaded() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.LOADED);

        assertThat(rack.getCellState()).isEqualTo(CellState.LOADED);
        assertThat(rack.getCellDisplayCode()).isEqualTo(4);
    }

    @Test
    @DisplayName("getCellState - INGRESS → INGRESS(1)")
    void cellState_ingress() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);
        rack.startIngress();

        assertThat(rack.getCellState()).isEqualTo(CellState.INGRESS);
        assertThat(rack.getCellDisplayCode()).isEqualTo(1);
    }

    @Test
    @DisplayName("getCellState - UNAVAILABLE → UNAVAILABLE(9)")
    void cellState_unavailable() {
        Rack rack = createRack("01010101", RackStatus.UNAVAILABLE, LuggageStatus.EMPTY);

        assertThat(rack.getCellState()).isEqualTo(CellState.UNAVAILABLE);
        assertThat(rack.getCellDisplayCode()).isEqualTo(9);
    }

    // ========== Persistable ==========

    @Test
    @DisplayName("isNew - createdAt이 null이면 true")
    void isNew() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        assertThat(rack.isNew()).isTrue();
    }

    @Test
    @DisplayName("getId - rackNo 반환")
    void getId() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);

        assertThat(rack.getId()).isEqualTo("01010101");
    }

    // ========== 헬퍼 메서드 ==========

    private Rack createRack(String rackNo, RackStatus status, LuggageStatus lugg) {
        return Rack.builder().rackNo(rackNo).storageId("STRG000001").status(status).lugg(lugg)
            .zoneCode("01").build();
    }
}

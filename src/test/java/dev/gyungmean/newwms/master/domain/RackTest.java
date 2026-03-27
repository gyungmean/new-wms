package dev.gyungmean.newwms.master.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RackTest {

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

    @Test
    @DisplayName("대기장 Rack 생성 - rackNo=00000000이어도 예외 없음, 파생 필드 null")
    void createStagingAreaRack() {
        Rack rack = Rack.builder()
                .rackNo("00000000").storageId("STRG_STAGING")
                .status(RackStatus.AVAILABLE).lugg(LuggageStatus.EMPTY).build();

        assertThat(rack.getRackNo()).isEqualTo("00000000");
        assertThat(rack.getStorageId()).isEqualTo("STRG_STAGING");
        assertThat(rack.getSidePos()).isNull();
        assertThat(rack.getSideRack()).isNull();
        assertThat(rack.getGroupId()).isNull();
        assertThat(rack.getRackSize()).isNull();
    }

    @Test
    @DisplayName("대기장 Rack에서 getAddress() 호출 시 예외")
    void getAddress_stagingAreaThrows() {
        Rack rack = Rack.builder()
                .rackNo("00000000").storageId("STRG_STAGING")
                .status(RackStatus.AVAILABLE).lugg(LuggageStatus.EMPTY).build();

        assertThatThrownBy(rack::getAddress).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("isAvailable - AVAILABLE + EMPTY이면 true")
    void isAvailable_true() {
        assertThat(createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY).isAvailable()).isTrue();
    }

    @Test
    @DisplayName("isAvailable - AVAILABLE + LOADED이면 false")
    void isAvailable_loadedIsFalse() {
        assertThat(createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.LOADED).isAvailable()).isFalse();
    }

    @Test
    @DisplayName("isAvailable - INGRESS 상태이면 false")
    void isAvailable_ingressIsFalse() {
        assertThat(createRack("01010101", RackStatus.INGRESS, LuggageStatus.EMPTY).isAvailable()).isFalse();
    }

    @Test
    @DisplayName("isDoubleDeepInner - z=1이면 true")
    void isDoubleDeepInner_z1() {
        assertThat(createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY).isDoubleDeepInner()).isTrue();
    }

    @Test
    @DisplayName("isDoubleDeepInner - z=2이면 false")
    void isDoubleDeepInner_z2() {
        assertThat(createRack("01020101", RackStatus.AVAILABLE, LuggageStatus.EMPTY).isDoubleDeepInner()).isFalse();
    }

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
        assertThatThrownBy(createRack("01010101", RackStatus.INGRESS, LuggageStatus.EMPTY)::startIngress)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("completeIngress - INGRESS 상태면 AVAILABLE+LOADED")
    void completeIngress_success() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);
        rack.startIngress();
        rack.completeIngress();
        assertThat(rack.getStatus()).isEqualTo(RackStatus.AVAILABLE);
        assertThat(rack.getLugg()).isEqualTo(LuggageStatus.LOADED);
    }

    @Test
    @DisplayName("completeIngress - INGRESS가 아니면 예외")
    void completeIngress_fail() {
        assertThatThrownBy(createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY)::completeIngress)
                .isInstanceOf(IllegalStateException.class);
    }

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
        assertThatThrownBy(createRack("01010101", RackStatus.OUTBOUND, LuggageStatus.EMPTY)::startOutbound)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("completeOutbound - OUTBOUND 상태면 AVAILABLE+EMPTY")
    void completeOutbound_success() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);
        rack.startOutbound();
        rack.completeOutbound();
        assertThat(rack.getStatus()).isEqualTo(RackStatus.AVAILABLE);
        assertThat(rack.getLugg()).isEqualTo(LuggageStatus.EMPTY);
    }

    @Test
    @DisplayName("completeOutbound - OUTBOUND가 아니면 예외")
    void completeOutbound_fail() {
        assertThatThrownBy(createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY)::completeOutbound)
                .isInstanceOf(IllegalStateException.class);
    }

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

    @Test
    @DisplayName("isNew - createdAt이 null이면 true")
    void isNew() {
        assertThat(createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY).isNew()).isTrue();
    }

    @Test
    @DisplayName("getId - RackId(storageId+rackNo) 반환")
    void getId() {
        Rack rack = createRack("01010101", RackStatus.AVAILABLE, LuggageStatus.EMPTY);
        assertThat(rack.getId()).isEqualTo(RackId.of("STRG000001", "01010101"));
        assertThat(rack.getRackNo()).isEqualTo("01010101");
        assertThat(rack.getStorageId()).isEqualTo("STRG000001");
    }

    private Rack createRack(String rackNo, RackStatus status, LuggageStatus lugg) {
        return Rack.builder().rackNo(rackNo).storageId("STRG000001").status(status).lugg(lugg)
            .zoneCode("01").build();
    }
}

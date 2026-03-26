package dev.gyungmean.newwms.master.domain;

import dev.gyungmean.newwms.master.domain.vo.RackAddress;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RackAddressTest {

    // ========== 파싱 ==========

    @Test
    @DisplayName("8자리 rackNo 파싱 - 정상")
    void parse_validRackNo() {
        // Given
        RackAddress addr = new RackAddress("08020301");

        // When
        // Then
        assertThat(addr.getS()).isEqualTo(8);
        assertThat(addr.getZ()).isEqualTo(2);
        assertThat(addr.getX()).isEqualTo(3);
        assertThat(addr.getY()).isEqualTo(1);


    }

    @Test
    @DisplayName("파싱 - null이면 예외")
    void parse_null() {
        //When
        //Then
        assertThatThrownBy(() -> new RackAddress(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("rackNo is null");
    }

    @Test
    @DisplayName("파싱 - s 범위 초과 시 예외")
    void parse_invalidS() {
        assertThatThrownBy(() -> new RackAddress("26010101"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("s is not correct");
    }

    @Test
    @DisplayName("파싱 - z 범위 초과 시 예외")
    void parse_invalidZ() {
        assertThatThrownBy(() -> new RackAddress("01100101"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("z is not correct");
    }

    @Test
    @DisplayName("파싱 - x 범위 초과 시 예외")
    void parse_invalidX() {
        assertThatThrownBy(() -> new RackAddress("01029901"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("x is not correct");
    }

    @Test
    @DisplayName("파싱 - y 범위 초과 시 예외")
    void parse_invalidY() {
        assertThatThrownBy(() -> new RackAddress("01020388"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("y is not correct");
    }

    // ========== toRackNo (round-trip) ==========

    @Test
    @DisplayName("toRackNo - 파싱 후 다시 조합하면 원본과 동일")
    void toRackNo_roundTrip() {
        String original = "08020301";
        RackAddress addr = new RackAddress(original);
        assertThat(addr.toRackNo()).isEqualTo(original);
    }

    // ========== derivePartnerRackNo ==========

    @Test
    @DisplayName("파트너 랙 - z=1이면 z=2로 변환")
    void derivePartner_z1_to_z2() {
        RackAddress addr = new RackAddress("08010301");
        assertThat(addr.derivePartnerRackNo()).isEqualTo("08020301");
    }

    @Test
    @DisplayName("파트너 랙 - z=2이면 z=1로 변환")
    void derivePartner_z2_to_z1() {
        RackAddress addr = new RackAddress("08020301");
        assertThat(addr.derivePartnerRackNo()).isEqualTo("08010301");
    }

    @Test
    @DisplayName("파트너 랙 - z=3이면 z=4로 변환")
    void derivePartner_z3_to_z4() {
        RackAddress addr = new RackAddress("08030301");
        assertThat(addr.derivePartnerRackNo()).isEqualTo("08040301");
    }

    @Test
    @DisplayName("파트너 랙 - z=4이면 z=3으로 변환")
    void derivePartner_z4_to_z3() {
        RackAddress addr = new RackAddress("08040301");
        assertThat(addr.derivePartnerRackNo()).isEqualTo("08030301");
    }

    // ========== deriveRackSize ==========

    @Test
    @DisplayName("랙 사이즈 - y=1~7이면 H(중량)")
    void deriveRackSize_heavy() {
        RackAddress addr = new RackAddress("01010105");
        assertThat(addr.deriveRackSize()).isEqualTo(RackSize.H);
    }

    @Test
    @DisplayName("랙 사이즈 - y=18이면 H(중량)")
    void deriveRackSize_y18_heavy() {
        RackAddress addr = new RackAddress("01010118");
        assertThat(addr.deriveRackSize()).isEqualTo(RackSize.H);
    }

    @Test
    @DisplayName("랙 사이즈 - y=8~17이면 M(경량)")
    void deriveRackSize_medium() {
        RackAddress addr = new RackAddress("01010110");
        assertThat(addr.deriveRackSize()).isEqualTo(RackSize.M);
    }

    // ========== deriveSidePosition ==========

    @Test
    @DisplayName("내측/외측 - z=1이면 INNER")
    void deriveSidePosition_z1_inner() {
        RackAddress addr = new RackAddress("01010101");
        assertThat(addr.deriveSidePosition()).isEqualTo(SidePosition.INNER);
    }

    @Test
    @DisplayName("내측/외측 - z=2이면 OUTER")
    void deriveSidePosition_z2_outer() {
        RackAddress addr = new RackAddress("01020101");
        assertThat(addr.deriveSidePosition()).isEqualTo(SidePosition.OUTER);
    }

    @Test
    @DisplayName("내측/외측 - z=3이면 OUTER")
    void deriveSidePosition_z3_outer() {
        RackAddress addr = new RackAddress("01030101");
        assertThat(addr.deriveSidePosition()).isEqualTo(SidePosition.OUTER);
    }

    @Test
    @DisplayName("내측/외측 - z=4이면 INNER")
    void deriveSidePosition_z4_inner() {
        RackAddress addr = new RackAddress("01040101");
        assertThat(addr.deriveSidePosition()).isEqualTo(SidePosition.INNER);
    }

    // ========== deriveGroupId ==========

    @Test
    @DisplayName("groupId - 문서 검증 예시: 01020101 → 1")
    void deriveGroupId_case1() {
        RackAddress addr = new RackAddress("01020101");
        assertThat(addr.deriveGroupId()).isEqualTo(1);
        // TODO: deriveGroupId()가 1인지 검증
    }

    @Test
    @DisplayName("groupId - 문서 검증 예시: 01010201 → 1 (같은 그룹)")
    void deriveGroupId_case2() {
        RackAddress addr = new RackAddress("01010201");
        assertThat(addr.deriveGroupId()).isEqualTo(1);
    }

    @Test
    @DisplayName("groupId - 문서 검증 예시: 01040101 → 2")
    void deriveGroupId_case3() {
        RackAddress addr = new RackAddress("01040101");
        assertThat(addr.deriveGroupId()).isEqualTo(2);
    }

    @Test
    @DisplayName("groupId - 문서 검증 예시: 24040201 → 48")
    void deriveGroupId_case4() {
        RackAddress addr = new RackAddress("24040201");
        assertThat(addr.deriveGroupId()).isEqualTo(48);
    }

    @Test
    @DisplayName("groupId - 문서 검증 예시: 01010102 → 49")
    void deriveGroupId_case5() {
        RackAddress addr = new RackAddress("01010102");
        assertThat(addr.deriveGroupId()).isEqualTo(49);
    }

    @Test
    @DisplayName("groupId - 문서 검증 예시: 13020103 → 121")
    void deriveGroupId_case6() {
        RackAddress addr = new RackAddress("13020103");
        assertThat(addr.deriveGroupId()).isEqualTo(121);
    }
}

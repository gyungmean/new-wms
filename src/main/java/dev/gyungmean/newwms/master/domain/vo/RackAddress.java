package dev.gyungmean.newwms.master.domain.vo;

import dev.gyungmean.newwms.master.domain.RackSize;
import dev.gyungmean.newwms.master.domain.SidePosition;
import lombok.Getter;

/**
 * 랙 주소 Value Object
 * 8자리 rack_no 문자열 "SSZZXXYY"를 s, z, x, y 좌표로 파싱한다.
 *
 * - s (호기/크레인): 01~24
 * - z (열): 01(내측), 02(외측) — Double-Deep 구조
 * - x (번지): 01~56
 * - y (단/높이): 01~18
 *
 * 사용 예:
 *   RackAddress addr = new RackAddress("08020102");
 *   addr.getS() → 8, addr.getZ() → 2, addr.getX() → 1, addr.getY() → 2
 *   addr.toRackNo() → "08020102"
 *   addr.derivePartnerRackNo() → "08010102" (z가 뒤집힘)
 *   addr.deriveRackSize() → RackSize.H or M (y 값 기준)
 *   addr.deriveSidePosition() → SidePosition.INNER or OUTER (z 값 기준)
 */
@Getter
public class RackAddress {

    private final int s;  // 호기 (크레인 번호)
    private final int z;  // 열 (1=내측, 2=외측)
    private final int x;  // 번지
    private final int y;  // 단 (높이)

    public RackAddress(String rackNo) {
        // TODO: 구현하세요
        // 1. null 체크, 길이 8 체크, 숫자만 포함 체크 → IllegalArgumentException
        // 2. substring으로 s(0~2), z(2~4), x(4~6), y(6~8) 파싱
        // 3. 범위 검증: s(1~24), z(1~2), x(1~56), y(1~18) → IllegalArgumentException
        throw new UnsupportedOperationException("RackAddress 생성자를 구현하세요");
    }

    /**
     * s, z, x, y를 다시 8자리 문자열로 조합
     * 예: s=8, z=2, x=1, y=2 → "08020102"
     */
    public String toRackNo() {
        // TODO: 구현하세요
        // String.format("%02d%02d%02d%02d", s, z, x, y) 활용
        throw new UnsupportedOperationException("toRackNo()를 구현하세요");
    }

    /**
     * Double-Deep 파트너 랙 번호 계산
     * z값을 뒤집는다: 1→2, 2→1
     * 예: "08010102" → "08020102"
     */
    public String derivePartnerRackNo() {
        // TODO: 구현하세요
        // z가 1이면 2로, 2이면 1로 바꿔서 toRackNo 형식으로 반환
        throw new UnsupportedOperationException("derivePartnerRackNo()를 구현하세요");
    }

    /**
     * y 좌표 기반 랙 사이즈 판단
     * H(중량): y = 1~7 또는 y = 18
     * M(경량): y = 8~17
     */
    public RackSize deriveRackSize() {
        // TODO: 구현하세요
        throw new UnsupportedOperationException("deriveRackSize()를 구현하세요");
    }

    /**
     * z 좌표 기반 내측/외측 판단
     * z=1 → INNER, z=2 → OUTER
     */
    public SidePosition deriveSidePosition() {
        // TODO: 구현하세요
        throw new UnsupportedOperationException("deriveSidePosition()를 구현하세요");
    }
}

package dev.gyungmean.newwms.master.domain;

import dev.gyungmean.newwms.common.domain.BaseEntity;
import dev.gyungmean.newwms.master.domain.vo.RackAddress;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "w_rack_i")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rack extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "rack_no", length = 8)
    private String rackNo;

    @Column(name = "storage_id", length = 10)
    private String storageId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RackStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "lugg", nullable = false)
    private LuggageStatus lugg;

    @Enumerated(EnumType.STRING)
    @Column(name = "side_pos")
    private SidePosition sidePos;

    @Column(name = "side_rack", length = 8)
    private String sideRack;

    @Column(name = "group_id")
    private Integer groupId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rack_size", length = 1)
    private RackSize rackSize;

    @Column(name = "zone_code", length = 2)
    private String zoneCode;

    @Column(name = "flexible_option")
    private Boolean flexibleOption;

    @Builder
    public Rack(String rackNo, String storageId, RackStatus status, LuggageStatus lugg,
                SidePosition sidePos, String sideRack, Integer groupId,
                RackSize rackSize, String zoneCode, Boolean flexibleOption) {
        this.rackNo = rackNo;
        this.storageId = storageId;
        this.status = status;
        this.lugg = lugg;
        this.sidePos = sidePos;
        this.sideRack = sideRack;
        this.groupId = groupId;
        this.rackSize = rackSize;
        this.zoneCode = zoneCode;
        this.flexibleOption = flexibleOption;
    }

    // ========== 도메인 메서드 (직접 구현하세요) ==========

    /**
     * 랙이 사용 가능한 상태인지 판단
     * 조건: status == AVAILABLE && lugg == EMPTY
     */
    public boolean isAvailable() {
        // TODO: 구현하세요
        throw new UnsupportedOperationException("isAvailable()을 구현하세요");
    }

    /**
     * Double-Deep 내측 랙인지 판단
     * 조건: sidePos == INNER
     */
    public boolean isDoubleDeepInner() {
        // TODO: 구현하세요
        throw new UnsupportedOperationException("isDoubleDeepInner()를 구현하세요");
    }

    /**
     * 파트너 랙 번호 반환 (Double-Deep 쌍)
     */
    public String getPartnerRackNo() {
        // TODO: 구현하세요 (sideRack 반환)
        throw new UnsupportedOperationException("getPartnerRackNo()를 구현하세요");
    }

    /**
     * 입고 시작: status → INGRESS
     * 전제조건: isAvailable() == true
     * 위반 시 IllegalStateException
     */
    public void startIngress() {
        // TODO: 구현하세요
        throw new UnsupportedOperationException("startIngress()를 구현하세요");
    }

    /**
     * 입고 완료: status → AVAILABLE, lugg → LOADED
     * 전제조건: status == INGRESS
     */
    public void completeIngress() {
        // TODO: 구현하세요
        throw new UnsupportedOperationException("completeIngress()를 구현하세요");
    }

    /**
     * 출고 시작: status → OUTBOUND
     * 전제조건: status == AVAILABLE && lugg == LOADED
     */
    public void startOutbound() {
        // TODO: 구현하세요
        throw new UnsupportedOperationException("startOutbound()를 구현하세요");
    }

    /**
     * 출고 완료: status → AVAILABLE, lugg → EMPTY
     * 전제조건: status == OUTBOUND
     */
    public void completeOutbound() {
        // TODO: 구현하세요
        throw new UnsupportedOperationException("completeOutbound()를 구현하세요");
    }

    /**
     * Cell 모니터링 표시 코드 반환 (status + lugg 조합)
     * 0=빈셀, 1=입고중, 2=출고중, 3=예약중, 4=적재, 5=이중입고, 6=공출고, 9=불가
     */
    public int getCellDisplayCode() {
        // TODO: 구현하세요
        // 힌트: status가 AVAILABLE일 때 lugg에 따라 0(EMPTY) 또는 4(LOADED)
        //       그 외에는 status의 code 값 반환
        throw new UnsupportedOperationException("getCellDisplayCode()를 구현하세요");
    }

    /**
     * rackNo로부터 RackAddress VO 생성 (비영속, on-demand)
     */
    public RackAddress getAddress() {
        return new RackAddress(this.rackNo);
    }

    // ========== Persistable ==========

    @Override
    public String getId() {
        return rackNo;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}

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

//    @Column(name = "flexible_option")
//    private Boolean flexibleOption;

    @Builder
    public Rack(String rackNo, String storageId, RackStatus status, LuggageStatus lugg,
        String zoneCode) {
        this.rackNo = rackNo;
        RackAddress address = getAddress();
        this.storageId = storageId;
        this.status = status;
        this.lugg = lugg;
        this.sidePos = address.deriveSidePosition();
        this.sideRack = address.derivePartnerRackNo();
        this.groupId = address.deriveGroupId();
        this.rackSize = address.deriveRackSize();
        this.zoneCode = zoneCode;
//        this.flexibleOption = flexibleOption;
    }

    // ========== 도메인 메서드 (직접 구현하세요) ==========

    /**
     * 랙이 사용 가능한 상태인지 판단 조건: status == AVAILABLE && lugg == EMPTY
     */
    public boolean isAvailable() {
        return ((status == RackStatus.AVAILABLE) && (lugg == LuggageStatus.EMPTY));
    }

    /**
     * Double-Deep 내측 랙인지 판단 조건: sidePos == INNER
     */
    public boolean isDoubleDeepInner() {
        return this.sidePos == SidePosition.INNER;
    }

    /**
     * 파트너 랙 번호 반환 (Double-Deep 쌍)
     */
    public String getPartnerRackNo() {
        return this.sideRack;
    }

    /**
     * 입고 시작: status → INGRESS 전제조건: isAvailable() == true 위반 시 IllegalStateException
     */
    public void startIngress() {
        if(!isAvailable()) {
            throw new IllegalStateException("입고할 수 없는 상태의 랙입니다.");
        }
        this.status = RackStatus.INGRESS;
    }

    /**
     * 입고 완료: status → AVAILABLE, lugg → LOADED 전제조건: status == INGRESS
     */
    public void completeIngress() {
        if(this.status != RackStatus.INGRESS) {
            throw new IllegalStateException("입고 중이지 않은 랙입니다.");
        }
        this.status = RackStatus.AVAILABLE;
        this.lugg = LuggageStatus.LOADED;
    }

    /**
     * 출고 시작: status → OUTBOUND 전제조건: status == AVAILABLE && lugg == LOADED
     */
    public void startOutbound() {
        if(!isAvailable()) {
            throw new IllegalStateException("출고할 수 없는 상태의 랙입니다.");
        }
        this.status = RackStatus.OUTBOUND;
    }

    /**
     * 출고 완료: status → AVAILABLE, lugg → EMPTY 전제조건: status == OUTBOUND
     */
    public void completeOutbound() {
        if(this.status != RackStatus.OUTBOUND) {
            throw new IllegalStateException("출고 중이지 않은 랙입니다.");
        }
        this.status = RackStatus.AVAILABLE;
        this.lugg = LuggageStatus.EMPTY;
    }

    /**
     * Cell 모니터링 상태 반환 (status + lugg 조합)
     */
    public CellState getCellState() {
        return CellState.of(this.status, this.lugg);
    }

    /**
     * Cell 모니터링 표시 코드 반환
     */
    public int getCellDisplayCode() {
        return getCellState().getDisplayCode();
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

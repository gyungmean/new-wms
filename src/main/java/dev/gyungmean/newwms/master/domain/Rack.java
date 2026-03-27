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
public class Rack extends BaseEntity implements Persistable<RackId> {

    @EmbeddedId
    private RackId id;

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

    @Builder
    public Rack(String rackNo, String storageId, RackStatus status, LuggageStatus lugg,
        String zoneCode) {
        this.id = RackId.of(storageId, rackNo);
        this.status = status;
        this.lugg = lugg;
        this.zoneCode = zoneCode;
        // 자동창고 형식(s/z/x/y 모두 1 이상)만 RackAddress 파싱. 대기장(00000000) 등은 null
        if (isAutoWarehouseRackNo(rackNo)) {
            RackAddress address = new RackAddress(rackNo);
            this.sidePos  = address.deriveSidePosition();
            this.sideRack = address.derivePartnerRackNo();
            this.groupId  = address.deriveGroupId();
            this.rackSize = address.deriveRackSize();
        }
    }

    // ========== 편의 메서드 ==========

    public String getRackNo() {
        return id.getRackNo();
    }

    public String getStorageId() {
        return id.getStorageId();
    }

    // ========== 도메인 메서드 ==========

    public boolean isAvailable() {
        return ((status == RackStatus.AVAILABLE) && (lugg == LuggageStatus.EMPTY));
    }

    public boolean isDoubleDeepInner() {
        return this.sidePos == SidePosition.INNER;
    }

    /** 파트너 랙 번호 반환. 비자동창고는 null. */
    public String getPartnerRackNo() {
        return this.sideRack;
    }

    public void startIngress() {
        if (!isAvailable()) throw new IllegalStateException("입고할 수 없는 상태의 랙입니다.");
        this.status = RackStatus.INGRESS;
    }

    public void completeIngress() {
        if (this.status != RackStatus.INGRESS) throw new IllegalStateException("입고 중이지 않은 랙입니다.");
        this.status = RackStatus.AVAILABLE;
        this.lugg = LuggageStatus.LOADED;
    }

    public void startOutbound() {
        if (!isAvailable()) throw new IllegalStateException("출고할 수 없는 상태의 랙입니다.");
        this.status = RackStatus.OUTBOUND;
    }

    public void completeOutbound() {
        if (this.status != RackStatus.OUTBOUND) throw new IllegalStateException("출고 중이지 않은 랙입니다.");
        this.status = RackStatus.AVAILABLE;
        this.lugg = LuggageStatus.EMPTY;
    }

    public CellState getCellState() {
        return CellState.of(this.status, this.lugg);
    }

    public int getCellDisplayCode() {
        return getCellState().getDisplayCode();
    }

    /** 자동창고 전용. 비자동창고에서 호출 시 IllegalStateException. */
    public RackAddress getAddress() {
        if (!isAutoWarehouseRackNo(this.id.getRackNo())) {
            throw new IllegalStateException("비자동창고 랙은 RackAddress를 지원하지 않습니다: " + this.id.getRackNo());
        }
        return new RackAddress(this.id.getRackNo());
    }

    // ========== Persistable ==========

    @Override
    public RackId getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }

    // ========== 내부 유틸 ==========

    private static boolean isAutoWarehouseRackNo(String rackNo) {
        if (rackNo == null || rackNo.length() != 8) return false;
        try {
            int s = Integer.parseInt(rackNo.substring(0, 2));
            int z = Integer.parseInt(rackNo.substring(2, 4));
            int x = Integer.parseInt(rackNo.substring(4, 6));
            int y = Integer.parseInt(rackNo.substring(6, 8));
            return s >= 1 && z >= 1 && x >= 1 && y >= 1;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

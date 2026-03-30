package dev.gyungmean.newwms.inventory.domain;

import dev.gyungmean.newwms.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "w_hold_ord_i")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HoldOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hold_order_id")
    private Long id;

    /** 보류 대상 재고 ID (Stock.id) */
    @Column(name = "stock_id", nullable = false)
    private Long stockId;

    /** 품목 코드 (조회 편의용) */
    @Column(name = "item_code", length = 18)
    private String itemCode;

    /** 보류 사유 */
    @Column(name = "reason", length = 200)
    private String reason;

    /** 보류 일시 */
    @Column(name = "hold_at")
    private LocalDateTime holdAt;

    /** 보류 해제 일시 */
    @Column(name = "unhold_at")
    private LocalDateTime unholdAt;

    /** 활성 여부 (false = 해제됨) */
    @Column(name = "active", nullable = false)
    private boolean active;

    private HoldOrder(Long stockId, String itemCode, String reason) {
        this.stockId = stockId;
        this.itemCode = itemCode;
        this.reason = reason;
        this.holdAt = LocalDateTime.now();
        this.active = true;
    }

    public static HoldOrder create(Long stockId, String itemCode, String reason) {
        // TODO (Wave 4 TDD)
        throw new UnsupportedOperationException("TODO");
    }

    public void deactivate() {
        // TODO (Wave 4 TDD)
        throw new UnsupportedOperationException("TODO");
    }
}

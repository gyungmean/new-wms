package dev.gyungmean.newwms.out.domain;

import dev.gyungmean.newwms.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 출고지시 — OutPlanDetail 1건에 대해 특정 Stock에서 얼마나 꺼낼지를 나타내는 엔티티.
 * OutPlanDetail 하나에 OutOrder 여러 개가 생길 수 있다 (FIFO로 여러 Stock에서 나눠서 꺼낼 경우).
 */
@Entity
@Table(name = "w_out_order")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutOrder extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어느 출고계획 상세에서 발생한 지시인지
    @Column(name = "deliver_ord_no", nullable = false, length = 10)
    private String deliverOrdNo;

    @Column(name = "deliver_ord_item", nullable = false)
    private Integer deliverOrdItem;

    // 어느 재고를 꺼낼지 (Stock PK)
    @Column(name = "stock_id", nullable = false)
    private Long stockId;

    // 이 지시에서 꺼낼 수량
    @Column(name = "allocated_qty", nullable = false)
    private BigDecimal allocatedQty;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OutOrderStatus status;

    public static OutOrder create(String deliverOrdNo, Integer deliverOrdItem,
                                   Long stockId, BigDecimal allocatedQty) {
        // TODO: 구현하세요
        // 힌트: status 초기값은 READY
        throw new UnsupportedOperationException("구현 필요");
    }

    public void complete() {
        // TODO: 구현하세요
        // 힌트: IN_PROGRESS 상태에서만 COMPLETED로 전이 가능
        throw new UnsupportedOperationException("구현 필요");
    }

    public void start() {
        // TODO: 구현하세요
        // 힌트: READY 상태에서만 IN_PROGRESS로 전이 가능
        throw new UnsupportedOperationException("구현 필요");
    }
}

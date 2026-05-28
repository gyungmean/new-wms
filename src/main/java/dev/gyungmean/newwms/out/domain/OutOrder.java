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
        OutOrder order = new OutOrder();
        order.deliverOrdNo = deliverOrdNo;
        order.deliverOrdItem = deliverOrdItem;
        order.stockId = stockId;
        order.allocatedQty = allocatedQty;
        order.status = OutOrderStatus.READY;
        return order;
    }

    public void complete() {
        if(this.status == OutOrderStatus.READY) {
            throw new IllegalStateException("완료할 출고 작업이 진행되지 않았습니다.");
        }
        if(this.status == OutOrderStatus.COMPLETED) {
            throw new IllegalStateException("이미 출고 작업이 완료 되었습니다.");
        }
        this.status = OutOrderStatus.COMPLETED;
    }

    public void start() {
        if(this.status == OutOrderStatus.IN_PROGRESS) {
            throw new IllegalStateException("이미 출고 작업이 진행 중입니다.");
        }
        if(this.status == OutOrderStatus.COMPLETED) {
            throw new IllegalStateException("이미 출고 작업이 완료 되었습니다.");
        }
        this.status = OutOrderStatus.IN_PROGRESS;
    }
}

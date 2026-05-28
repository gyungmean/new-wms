package dev.gyungmean.newwms.out.domain;

import dev.gyungmean.newwms.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "w_outplan_d")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutPlanDetail extends BaseEntity {

    @EmbeddedId
    private OutPlanDetailId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("deliverOrdNo")
    @JoinColumn(name = "deliver_ord_no")
    private OutPlan outPlan;

    @Column(name = "item_code", nullable = false, length = 18)
    private String itemCode;

    @Column(name = "batch_no", length = 10)
    private String batchNo;

    @Column(name = "load_type", length = 4)
    private String loadType;

    @Column(name = "order_qty", nullable = false)
    private BigDecimal orderQty;

    @Column(name = "progress_qty", nullable = false)
    private BigDecimal progressQty = BigDecimal.ZERO;

    @Column(name = "complete_qty", nullable = false)
    private BigDecimal completeQty = BigDecimal.ZERO;

    @Column(name = "item_unit", length = 3)
    private String itemUnit;

    @Column(name = "option_pallet_code", length = 20)
    private String optionPalletCode;

    @Column(name = "plant", length = 4)
    private String plant;

    @Column(name = "stor_loc", length = 4)
    private String storLoc;

    @Column(name = "booking_no", length = 20)
    private String bookingNo;

    public static OutPlanDetail create(OutPlan outPlan, int seq,
                                       String itemCode, String batchNo, String loadType,
                                       BigDecimal orderQty, String itemUnit,
                                       String plant, String storLoc) {
        OutPlanDetail detail = new OutPlanDetail();
        detail.id = OutPlanDetailId.of(outPlan.getDeliverOrdNo(), seq);
        detail.outPlan = outPlan;
        detail.itemCode = itemCode;
        detail.batchNo = batchNo;
        detail.loadType = loadType;
        detail.orderQty = orderQty;
        detail.progressQty = BigDecimal.ZERO;
        detail.completeQty = BigDecimal.ZERO;
        detail.itemUnit = itemUnit;
        detail.plant = plant;
        detail.storLoc = storLoc;
        return detail;
    }

    public void addProgress(BigDecimal qty) {
        this.progressQty = this.progressQty.add(qty);
    }

    public void complete(BigDecimal qty) {
        this.completeQty = this.completeQty.add(qty);
        if (this.completeQty.compareTo(this.orderQty) > 0) {
            throw new IllegalStateException("완료 수량이 주문 수량을 초과합니다.");
        }
    }

    public boolean isCompleted() {
        return completeQty.compareTo(orderQty) >= 0;
    }
}

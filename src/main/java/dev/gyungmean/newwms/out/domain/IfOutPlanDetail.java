package dev.gyungmean.newwms.out.domain;

import dev.gyungmean.newwms.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "if_outplan_d")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IfOutPlanDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "if_outplan_id", nullable = false)
    private IfOutPlan ifOutPlan;

    @Column(name = "seq", nullable = false)
    private Integer seq;

    @Column(name = "item_code", nullable = false, length = 18)
    private String itemCode;

    @Column(name = "batch_no", length = 10)
    private String batchNo;

    @Column(name = "load_type", length = 4)
    private String loadType;

    @Column(name = "order_qty", nullable = false)
    private BigDecimal orderQty;

    @Column(name = "item_unit", length = 3)
    private String itemUnit;

    @Column(name = "plant", length = 4)
    private String plant;

    @Column(name = "stor_loc", length = 4)
    private String storLoc;

    public static IfOutPlanDetail create(IfOutPlan ifOutPlan, int seq,
                                          String itemCode, String batchNo, String loadType,
                                          BigDecimal orderQty, String itemUnit,
                                          String plant, String storLoc) {
        IfOutPlanDetail detail = new IfOutPlanDetail();
        detail.ifOutPlan = ifOutPlan;
        detail.seq = seq;
        detail.itemCode = itemCode;
        detail.batchNo = batchNo;
        detail.loadType = loadType;
        detail.orderQty = orderQty;
        detail.itemUnit = itemUnit;
        detail.plant = plant;
        detail.storLoc = storLoc;
        return detail;
    }
}

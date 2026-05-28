package dev.gyungmean.newwms.out.domain;

import dev.gyungmean.newwms.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "w_outplan_m")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutPlan extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "deliver_ord_no", length = 10)
    private String deliverOrdNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "output_type", nullable = false)
    private OutputType outputType;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", length = 20, nullable = false)
    private OutPlanStatus orderStatus;

    @Column(name = "vehicle_no", length = 30)
    private String vehicleNo;

    @Column(name = "vehicle_code", length = 10)
    private String vehicleCode;

    @Column(name = "driver_code", length = 10)
    private String driverCode;

    @Column(name = "customer_code", length = 10)
    private String customerCode;

    @Column(name = "sold_code", length = 10)
    private String soldCode;

    @Column(name = "vendor_code", length = 10)
    private String vendorCode;

    @OneToMany(mappedBy = "outPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OutPlanDetail> details = new ArrayList<>();

    public static OutPlan create(String deliverOrdNo, OutputType outputType,
                                  String vehicleNo, String vehicleCode, String driverCode,
                                  String customerCode, String soldCode, String vendorCode) {
        OutPlan plan = new OutPlan();
        plan.deliverOrdNo = deliverOrdNo;
        plan.outputType = outputType;
        plan.orderStatus = OutPlanStatus.WAITING;
        plan.vehicleNo = vehicleNo;
        plan.vehicleCode = vehicleCode;
        plan.driverCode = driverCode;
        plan.customerCode = customerCode;
        plan.soldCode = soldCode;
        plan.vendorCode = vendorCode;
        return plan;
    }

    public OutPlanDetail addDetail(String itemCode, String batchNo, String loadType,
                                    BigDecimal orderQty, String itemUnit,
                                    String plant, String storLoc) {
        OutPlanDetail detail = OutPlanDetail.create(this, details.size() + 1,
                itemCode, batchNo, loadType, orderQty, itemUnit, plant, storLoc);
        details.add(detail);
        return detail;
    }

    public List<OutPlanDetail> getDetails() {
        return Collections.unmodifiableList(details);
    }

    public void confirmLoad() {
        if (orderStatus != OutPlanStatus.LOADING_WAIT) {
            throw new IllegalStateException("상차 대기 상태일 때만 상차 확정이 가능합니다.");
        }
        this.orderStatus = OutPlanStatus.LOAD_CONFIRMED;
    }

    public void startLoading() {
        if (orderStatus != OutPlanStatus.WAITING) {
            throw new IllegalStateException("출고 대기 상태일 때만 상차 대기로 전환할 수 있습니다.");
        }
        this.orderStatus = OutPlanStatus.LOADING_WAIT;
    }

    @Override
    public String getId() {
        return deliverOrdNo;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}

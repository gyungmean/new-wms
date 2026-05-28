package dev.gyungmean.newwms.out.domain;

import dev.gyungmean.newwms.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * ERP → WMS 인터페이스 스테이징 테이블.
 * ERP가 출고오더를 내려보내면 여기에 쌓이고, 스케줄러가 읽어서 OutPlan으로 변환한다.
 */
@Entity
@Table(name = "if_outplan")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IfOutPlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deliver_ord_no", nullable = false, length = 10, unique = true)
    private String deliverOrdNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "output_type", nullable = false)
    private OutputType outputType;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "if_status", nullable = false, length = 20)
    private IfStatus ifStatus;

    @Column(name = "error_message")
    private String errorMessage;

    @OneToMany(mappedBy = "ifOutPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IfOutPlanDetail> details = new ArrayList<>();

    public static IfOutPlan create(String deliverOrdNo, OutputType outputType,
                                    String vehicleNo, String vehicleCode, String driverCode,
                                    String customerCode, String soldCode, String vendorCode) {
        IfOutPlan ifOutPlan = new IfOutPlan();
        ifOutPlan.deliverOrdNo = deliverOrdNo;
        ifOutPlan.outputType = outputType;
        ifOutPlan.vehicleNo = vehicleNo;
        ifOutPlan.vehicleCode = vehicleCode;
        ifOutPlan.driverCode = driverCode;
        ifOutPlan.customerCode = customerCode;
        ifOutPlan.soldCode = soldCode;
        ifOutPlan.vendorCode = vendorCode;
        ifOutPlan.ifStatus = IfStatus.PENDING;
        return ifOutPlan;
    }

    public IfOutPlanDetail addDetail(String itemCode, String batchNo, String loadType,
                                      java.math.BigDecimal orderQty, String itemUnit,
                                      String plant, String storLoc) {
        IfOutPlanDetail detail = IfOutPlanDetail.create(this, details.size() + 1,
                itemCode, batchNo, loadType, orderQty, itemUnit, plant, storLoc);
        details.add(detail);
        return detail;
    }

    public void markProcessed() {
        this.ifStatus = IfStatus.PROCESSED;
        this.errorMessage = null;
    }

    public void markError(String message) {
        this.ifStatus = IfStatus.ERROR;
        this.errorMessage = message;
    }
}

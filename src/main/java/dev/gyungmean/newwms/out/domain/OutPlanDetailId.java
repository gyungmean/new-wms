package dev.gyungmean.newwms.out.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class OutPlanDetailId implements Serializable {

    @Column(name = "deliver_ord_no", length = 10, nullable = false)
    private String deliverOrdNo;

    @Column(name = "deliver_ord_item", nullable = false)
    private Integer deliverOrdItem;

    public static OutPlanDetailId of(String deliverOrdNo, Integer deliverOrdItem) {
        OutPlanDetailId id = new OutPlanDetailId();
        id.deliverOrdNo = deliverOrdNo;
        id.deliverOrdItem = deliverOrdItem;
        return id;
    }
}

package dev.gyungmean.newwms.master.domain;

import dev.gyungmean.newwms.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

import java.math.BigDecimal;

@Entity
@Table(name = "w_load_type_i")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoadType extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "load_type", length = 2)
    private String loadType;

    @Enumerated(EnumType.STRING)
    @Column(name = "bag_type", length = 1)
    private BagType bagType;

    @Column(name = "layer")
    private Integer layer;

    @Column(name = "base_weight")
    private BigDecimal baseWeight;

    @Column(name = "remark")
    private String remark;

    @Column(name = "usage_yn", length = 1)
    private String usageYn;

    @Builder
    public LoadType(String loadType, BagType bagType, Integer layer,
                    BigDecimal baseWeight, String remark, String usageYn) {
        this.loadType = loadType;
        this.bagType = bagType;
        this.layer = layer;
        this.baseWeight = baseWeight;
        this.remark = remark;
        this.usageYn = usageYn;
    }

    @Override
    public String getId() {
        return loadType;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}

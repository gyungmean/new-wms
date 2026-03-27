package dev.gyungmean.newwms.master.domain;

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
public class RackId implements Serializable {

    @Column(name = "storage_id", length = 10, nullable = false)
    private String storageId;

    @Column(name = "rack_no", length = 8, nullable = false)
    private String rackNo;

    public static RackId of(String storageId, String rackNo) {
        RackId id = new RackId();
        id.storageId = storageId;
        id.rackNo = rackNo;
        return id;
    }
}

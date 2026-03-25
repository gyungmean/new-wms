package dev.gyungmean.newwms.master.domain;

import dev.gyungmean.newwms.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "w_strg_i")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Storage extends BaseEntity implements Persistable<String> {

    @Id
    @Column(name = "storage_id", length = 10)
    private String storageId;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_kind_code", length = 1)
    private StorageKind storageKindCode;

    @Column(name = "storage_name", length = 50)
    private String storageName;

    @Builder
    public Storage(String storageId, StorageKind storageKindCode, String storageName) {
        this.storageId = storageId;
        this.storageKindCode = storageKindCode;
        this.storageName = storageName;
    }

    public void update(StorageKind storageKindCode, String storageName) {
        this.storageKindCode = storageKindCode;
        this.storageName = storageName;
    }

    @Override
    public String getId() {
        return storageId;
    }

    @Override
    public boolean isNew() {
        return getCreatedAt() == null;
    }
}

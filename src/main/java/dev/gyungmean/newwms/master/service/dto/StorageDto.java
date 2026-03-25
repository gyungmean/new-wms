package dev.gyungmean.newwms.master.service.dto;

import dev.gyungmean.newwms.master.domain.Storage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StorageDto {

    private String storageId;
    private String storageKindCode;
    private String storageName;

    public static StorageDto from(Storage storage) {
        return StorageDto.builder()
                .storageId(storage.getStorageId())
                .storageKindCode(storage.getStorageKindCode() != null ? storage.getStorageKindCode().name() : null)
                .storageName(storage.getStorageName())
                .build();
    }
}

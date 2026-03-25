package dev.gyungmean.newwms.master.controller.req;

import dev.gyungmean.newwms.master.domain.StorageKind;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StorageCreateReq {

    @NotBlank
    private String storageId;

    @NotNull
    private StorageKind storageKindCode;

    private String storageName;
}

package dev.gyungmean.newwms.master.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StorageTest {

    @Test
    @DisplayName("Storage 엔티티 생성")
    void createStorage() {
        Storage storage = Storage.builder()
                .storageId("STRG000001")
                .storageKindCode(StorageKind.A)
                .storageName("1호 자동창고")
                .build();

        assertThat(storage.getStorageId()).isEqualTo("STRG000001");
        assertThat(storage.getStorageKindCode()).isEqualTo(StorageKind.A);
        assertThat(storage.getStorageName()).isEqualTo("1호 자동창고");
    }

    @Test
    @DisplayName("Storage update 메서드")
    void updateStorage() {
        Storage storage = Storage.builder()
                .storageId("STRG000001")
                .storageKindCode(StorageKind.A)
                .storageName("원래 이름")
                .build();

        storage.update(StorageKind.M, "변경된 이름");

        assertThat(storage.getStorageKindCode()).isEqualTo(StorageKind.M);
        assertThat(storage.getStorageName()).isEqualTo("변경된 이름");
    }

    @Test
    @DisplayName("Storage isNew - createdAt이 null이면 true")
    void isNewWhenCreatedAtNull() {
        Storage storage = Storage.builder()
                .storageId("STRG000001")
                .storageKindCode(StorageKind.A)
                .storageName("테스트")
                .build();

        assertThat(storage.isNew()).isTrue();
    }

    @Test
    @DisplayName("Storage getId는 storageId 반환")
    void getId() {
        Storage storage = Storage.builder()
                .storageId("STRG000001")
                .storageKindCode(StorageKind.A)
                .build();

        assertThat(storage.getId()).isEqualTo("STRG000001");
    }

    @Test
    @DisplayName("StorageKind enum 값 확인")
    void storageKindValues() {
        assertThat(StorageKind.A.getDescription()).isEqualTo("자동창고");
        assertThat(StorageKind.M.getDescription()).isEqualTo("수동창고");
    }
}

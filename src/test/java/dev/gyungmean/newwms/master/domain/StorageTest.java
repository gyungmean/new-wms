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

        // TODO: storageId, storageKindCode, storageName 검증
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

        // TODO: storageKindCode가 M, storageName이 "변경된 이름"인지 검증
    }

    @Test
    @DisplayName("Storage isNew - createdAt이 null이면 true")
    void isNewWhenCreatedAtNull() {
        Storage storage = Storage.builder()
                .storageId("STRG000001")
                .storageKindCode(StorageKind.A)
                .storageName("테스트")
                .build();

        // TODO: isNew()가 true인지 검증
    }

    @Test
    @DisplayName("Storage getId는 storageId 반환")
    void getId() {
        Storage storage = Storage.builder()
                .storageId("STRG000001")
                .storageKindCode(StorageKind.A)
                .build();

        // TODO: getId()가 "STRG000001"인지 검증
    }

    @Test
    @DisplayName("StorageKind enum 값 확인")
    void storageKindValues() {
        // TODO: StorageKind.A의 description이 "자동창고", M이 "수동창고"인지 검증
    }
}

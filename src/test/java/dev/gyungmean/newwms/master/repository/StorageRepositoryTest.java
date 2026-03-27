package dev.gyungmean.newwms.master.repository;

import dev.gyungmean.newwms.master.domain.Storage;
import dev.gyungmean.newwms.master.domain.StorageKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class StorageRepositoryTest {

    @Autowired
    private StorageRepository storageRepository;

    @BeforeEach
    void setUp() {
        storageRepository.deleteAll();
    }

    @Test
    @DisplayName("Storage 저장 및 조회")
    void saveAndFind() {
        Storage storage = createStorage("STRG000001", StorageKind.A, "1호 자동창고");

        storageRepository.save(storage);

        Optional<Storage> found = storageRepository.findById("STRG000001");
        assertThat(found).isPresent();
        assertThat(found.get().getStorageName()).isEqualTo("1호 자동창고");
    }

    @Test
    @DisplayName("StorageKind로 조회")
    void findByStorageKindCode() {
        storageRepository.save(createStorage("STRG000001", StorageKind.A, "자동창고1"));
        storageRepository.save(createStorage("STRG000002", StorageKind.M, "수동창고1"));
        storageRepository.save(createStorage("STRG000003", StorageKind.A, "자동창고2"));

        List<Storage> result = storageRepository.findByStorageKindCode(StorageKind.A);
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Storage 삭제")
    void delete() {
        storageRepository.save(createStorage("STRG000001", StorageKind.A, "삭제 대상"));

        storageRepository.deleteById("STRG000001");

        assertThat(storageRepository.findById("STRG000001")).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 ID 조회 시 empty")
    void findById_notFound() {
        assertThat(storageRepository.findById("NOTEXIST")).isEmpty();
    }

    // ========== 헬퍼 메서드 ==========

    private Storage createStorage(String id, StorageKind kind, String name) {
        return Storage.builder()
                .storageId(id)
                .storageKindCode(kind)
                .storageName(name)
                .build();
    }
}

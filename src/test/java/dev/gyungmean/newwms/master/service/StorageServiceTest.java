package dev.gyungmean.newwms.master.service;

import dev.gyungmean.newwms.master.controller.req.StorageCreateReq;
import dev.gyungmean.newwms.master.domain.Storage;
import dev.gyungmean.newwms.master.domain.StorageKind;
import dev.gyungmean.newwms.master.repository.StorageRepository;
import dev.gyungmean.newwms.master.service.dto.StorageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {

    @Mock
    private StorageRepository storageRepository;

    @InjectMocks
    private StorageService storageService;

    // ========== create ==========

    @Test
    @DisplayName("저장소 생성 성공")
    void create_success() {
        StorageCreateReq req = createReq("STRG000001", StorageKind.A, "1호 자동창고");

        given(storageRepository.existsById("STRG000001")).willReturn(false);
        given(storageRepository.save(any(Storage.class))).willAnswer(inv -> inv.getArgument(0));

        // TODO: storageService.create(req) 호출 후 결과 검증
        // TODO: verify(storageRepository).save(any(Storage.class))
    }

    @Test
    @DisplayName("저장소 생성 실패 - 중복 ID")
    void create_duplicate() {
        StorageCreateReq req = createReq("STRG000001", StorageKind.A, "중복");

        given(storageRepository.existsById("STRG000001")).willReturn(true);

        // TODO: create() 호출 시 IllegalArgumentException + "이미 존재하는" 메시지 검증
    }

    // ========== findById ==========

    @Test
    @DisplayName("저장소 조회 성공")
    void findById_success() {
        Storage storage = Storage.builder()
                .storageId("STRG000001")
                .storageKindCode(StorageKind.A)
                .storageName("1호 자동창고")
                .build();

        given(storageRepository.findById("STRG000001")).willReturn(Optional.of(storage));

        // TODO: findById 호출 후 storageId, storageName 검증
    }

    @Test
    @DisplayName("저장소 조회 실패 - 존재하지 않음")
    void findById_notFound() {
        given(storageRepository.findById("NOTEXIST")).willReturn(Optional.empty());

        // TODO: findById 호출 시 IllegalArgumentException 검증
    }

    // ========== findAll ==========

    @Test
    @DisplayName("저장소 전체 조회")
    void findAll() {
        List<Storage> storages = List.of(
                Storage.builder().storageId("STRG01").storageKindCode(StorageKind.A).storageName("자동1").build(),
                Storage.builder().storageId("STRG02").storageKindCode(StorageKind.M).storageName("수동1").build()
        );

        given(storageRepository.findAll()).willReturn(storages);

        // TODO: findAll() 호출 후 size가 2인지 검증
    }

    // ========== update ==========

    @Test
    @DisplayName("저장소 수정 성공")
    void update_success() {
        Storage storage = Storage.builder()
                .storageId("STRG000001")
                .storageKindCode(StorageKind.A)
                .storageName("원래 이름")
                .build();

        StorageCreateReq req = createReq("STRG000001", StorageKind.M, "변경된 이름");
        given(storageRepository.findById("STRG000001")).willReturn(Optional.of(storage));

        // TODO: update() 호출 후 storageKindCode, storageName 변경 검증
    }

    @Test
    @DisplayName("저장소 수정 실패 - 존재하지 않음")
    void update_notFound() {
        StorageCreateReq req = createReq("NOTEXIST", StorageKind.A, "없음");
        given(storageRepository.findById("NOTEXIST")).willReturn(Optional.empty());

        // TODO: update() 호출 시 IllegalArgumentException 검증
    }

    // ========== delete ==========

    @Test
    @DisplayName("저장소 삭제 성공")
    void delete_success() {
        given(storageRepository.existsById("STRG000001")).willReturn(true);

        // TODO: delete() 호출 후 verify(storageRepository).deleteById("STRG000001")
    }

    @Test
    @DisplayName("저장소 삭제 실패 - 존재하지 않음")
    void delete_notFound() {
        given(storageRepository.existsById("NOTEXIST")).willReturn(false);

        // TODO: delete() 호출 시 IllegalArgumentException 검증
    }

    // ========== 헬퍼 메서드 ==========

    private StorageCreateReq createReq(String storageId, StorageKind kind, String name) {
        try {
            StorageCreateReq req = new StorageCreateReq();

            var idField = StorageCreateReq.class.getDeclaredField("storageId");
            idField.setAccessible(true);
            idField.set(req, storageId);

            var kindField = StorageCreateReq.class.getDeclaredField("storageKindCode");
            kindField.setAccessible(true);
            kindField.set(req, kind);

            var nameField = StorageCreateReq.class.getDeclaredField("storageName");
            nameField.setAccessible(true);
            nameField.set(req, name);

            return req;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

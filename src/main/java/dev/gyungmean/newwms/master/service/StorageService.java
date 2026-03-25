package dev.gyungmean.newwms.master.service;

import dev.gyungmean.newwms.master.controller.req.StorageCreateReq;
import dev.gyungmean.newwms.master.domain.Storage;
import dev.gyungmean.newwms.master.repository.StorageRepository;
import dev.gyungmean.newwms.master.service.dto.StorageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StorageService {

    private final StorageRepository storageRepository;

    @Transactional
    public StorageDto create(StorageCreateReq req) {
        if (storageRepository.existsById(req.getStorageId())) {
            throw new IllegalArgumentException("이미 존재하는 저장소입니다: " + req.getStorageId());
        }

        Storage storage = Storage.builder()
                .storageId(req.getStorageId())
                .storageKindCode(req.getStorageKindCode())
                .storageName(req.getStorageName())
                .build();

        return StorageDto.from(storageRepository.save(storage));
    }

    public StorageDto findById(String storageId) {
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new IllegalArgumentException("저장소를 찾을 수 없습니다: " + storageId));
        return StorageDto.from(storage);
    }

    public List<StorageDto> findAll() {
        return storageRepository.findAll().stream()
                .map(StorageDto::from)
                .toList();
    }

    @Transactional
    public StorageDto update(String storageId, StorageCreateReq req) {
        Storage storage = storageRepository.findById(storageId)
                .orElseThrow(() -> new IllegalArgumentException("저장소를 찾을 수 없습니다: " + storageId));

        storage.update(req.getStorageKindCode(), req.getStorageName());
        return StorageDto.from(storage);
    }

    @Transactional
    public void delete(String storageId) {
        if (!storageRepository.existsById(storageId)) {
            throw new IllegalArgumentException("저장소를 찾을 수 없습니다: " + storageId);
        }
        storageRepository.deleteById(storageId);
    }
}

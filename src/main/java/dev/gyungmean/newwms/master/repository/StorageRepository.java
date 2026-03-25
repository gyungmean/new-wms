package dev.gyungmean.newwms.master.repository;

import dev.gyungmean.newwms.master.domain.Storage;
import dev.gyungmean.newwms.master.domain.StorageKind;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StorageRepository extends JpaRepository<Storage, String> {

    List<Storage> findByStorageKindCode(StorageKind storageKindCode);
}

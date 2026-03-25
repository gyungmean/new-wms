package dev.gyungmean.newwms.master.repository;

import dev.gyungmean.newwms.master.domain.Rack;
import dev.gyungmean.newwms.master.domain.RackStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RackRepository extends JpaRepository<Rack, String> {

    List<Rack> findByStorageId(String storageId);

    List<Rack> findByStatus(RackStatus status);

    List<Rack> findByZoneCode(String zoneCode);

    List<Rack> findByStorageIdAndStatus(String storageId, RackStatus status);
}

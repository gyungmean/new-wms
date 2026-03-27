package dev.gyungmean.newwms.master.repository;

import dev.gyungmean.newwms.master.domain.Rack;
import dev.gyungmean.newwms.master.domain.RackId;
import dev.gyungmean.newwms.master.domain.RackStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RackRepository extends JpaRepository<Rack, RackId> {

    @Query("SELECT r FROM Rack r WHERE r.id.storageId = :storageId")
    List<Rack> findByStorageId(@Param("storageId") String storageId);

    List<Rack> findByStatus(RackStatus status);

    List<Rack> findByZoneCode(String zoneCode);

    @Query("SELECT r FROM Rack r WHERE r.id.storageId = :storageId AND r.status = :status")
    List<Rack> findByStorageIdAndStatus(@Param("storageId") String storageId,
                                        @Param("status") RackStatus status);
}

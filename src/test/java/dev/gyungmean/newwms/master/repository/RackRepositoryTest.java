package dev.gyungmean.newwms.master.repository;

import dev.gyungmean.newwms.master.domain.*;
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
class RackRepositoryTest {

    @Autowired
    private RackRepository rackRepository;

    @BeforeEach
    void setUp() {
        rackRepository.deleteAll();
    }

    @Test
    @DisplayName("Rack 저장 및 조회")
    void saveAndFind() {
        Rack rack = createRack("01010101", "STRG01", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01");

        rackRepository.save(rack);

        // TODO: findById로 조회 후 isPresent, rackNo, 자동파생 필드(sidePos 등) 검증
        Optional<Rack> found = rackRepository.findById("01010101");
        assertThat(found).isPresent();
        assertThat(found.get().getRackNo()).isEqualTo("01010101");
        assertThat(found.get().getSidePos()).isEqualTo(SidePosition.INNER);
    }

    @Test
    @DisplayName("storageId로 조회")
    void findByStorageId() {
        rackRepository.save(createRack("01010101", "STRG01", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01"));
        rackRepository.save(createRack("01020101", "STRG01", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01"));
        rackRepository.save(createRack("02010101", "STRG02", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01"));

        List<Rack> result = rackRepository.findByStorageId("STRG01");
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("status로 조회")
    void findByStatus() {
        rackRepository.save(createRack("01010101", "STRG01", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01"));
        rackRepository.save(createRack("01020101", "STRG01", RackStatus.INGRESS, LuggageStatus.EMPTY, "01"));
        rackRepository.save(createRack("02010101", "STRG01", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01"));

        assertThat(rackRepository.findByStatus(RackStatus.AVAILABLE)).hasSize(2);
        assertThat(rackRepository.findByStatus(RackStatus.INGRESS)).hasSize(1);
    }

    @Test
    @DisplayName("zoneCode로 조회")
    void findByZoneCode() {
        rackRepository.save(createRack("01010101", "STRG01", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01"));
        rackRepository.save(createRack("01020101", "STRG01", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "02"));

        assertThat(rackRepository.findByZoneCode("01")).hasSize(1);
    }

    @Test
    @DisplayName("storageId + status 복합 조회")
    void findByStorageIdAndStatus() {
        rackRepository.save(createRack("01010101", "STRG01", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01"));
        rackRepository.save(createRack("01020101", "STRG01", RackStatus.INGRESS, LuggageStatus.EMPTY, "01"));
        rackRepository.save(createRack("02010101", "STRG02", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01"));

        List<Rack> result = rackRepository.findByStorageIdAndStatus("STRG01", RackStatus.AVAILABLE);
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Rack 삭제")
    void delete() {
        rackRepository.save(createRack("01010101", "STRG01", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01"));

        rackRepository.deleteById("01010101");

        assertThat(rackRepository.findById("01010101")).isEmpty();
    }

    // ========== 헬퍼 메서드 ==========

    private Rack createRack(String rackNo, String storageId, RackStatus status,
                            LuggageStatus lugg, String zoneCode) {
        return Rack.builder()
                .rackNo(rackNo)
                .storageId(storageId)
                .status(status)
                .lugg(lugg)
                .zoneCode(zoneCode)
                .build();
    }
}

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
        rackRepository.save(createRack("01010101", "STRG01", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01"));

        Optional<Rack> found = rackRepository.findById(RackId.of("STRG01", "01010101"));
        assertThat(found).isPresent();
        assertThat(found.get().getRackNo()).isEqualTo("01010101");
        assertThat(found.get().getStorageId()).isEqualTo("STRG01");
        assertThat(found.get().getSidePos()).isEqualTo(SidePosition.INNER);
    }

    @Test
    @DisplayName("동일 rackNo가 다른 storageId에 공존 가능")
    void sameRackNoDifferentStorage() {
        rackRepository.save(createRack("01010101", "STRG01", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01"));
        rackRepository.save(createRack("01010101", "STRG02", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01"));

        assertThat(rackRepository.findAll()).hasSize(2);
        assertThat(rackRepository.findById(RackId.of("STRG01", "01010101"))).isPresent();
        assertThat(rackRepository.findById(RackId.of("STRG02", "01010101"))).isPresent();
    }

    @Test
    @DisplayName("대기장 rackNo=00000000이 storageId별로 여러 개 저장 가능")
    void stagingAreaMultipleStorage() {
        rackRepository.save(Rack.builder().rackNo("00000000").storageId("STRG_A")
                .status(RackStatus.AVAILABLE).lugg(LuggageStatus.EMPTY).build());
        rackRepository.save(Rack.builder().rackNo("00000000").storageId("STRG_B")
                .status(RackStatus.AVAILABLE).lugg(LuggageStatus.EMPTY).build());

        assertThat(rackRepository.findAll()).hasSize(2);
        assertThat(rackRepository.findById(RackId.of("STRG_A", "00000000"))).isPresent();
        assertThat(rackRepository.findById(RackId.of("STRG_B", "00000000"))).isPresent();
    }

    @Test
    @DisplayName("storageId로 조회")
    void findByStorageId() {
        rackRepository.save(createRack("01010101", "STRG01", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01"));
        rackRepository.save(createRack("01020101", "STRG01", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01"));
        rackRepository.save(createRack("02010101", "STRG02", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01"));

        assertThat(rackRepository.findByStorageId("STRG01")).hasSize(2);
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

        assertThat(rackRepository.findByStorageIdAndStatus("STRG01", RackStatus.AVAILABLE)).hasSize(1);
    }

    @Test
    @DisplayName("Rack 삭제")
    void delete() {
        rackRepository.save(createRack("01010101", "STRG01", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01"));

        rackRepository.deleteById(RackId.of("STRG01", "01010101"));

        assertThat(rackRepository.findById(RackId.of("STRG01", "01010101"))).isEmpty();
    }

    private Rack createRack(String rackNo, String storageId, RackStatus status,
                            LuggageStatus lugg, String zoneCode) {
        return Rack.builder().rackNo(rackNo).storageId(storageId)
                .status(status).lugg(lugg).zoneCode(zoneCode).build();
    }
}

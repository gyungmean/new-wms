package dev.gyungmean.newwms.master.service;

import dev.gyungmean.newwms.master.controller.req.RackCreateReq;
import dev.gyungmean.newwms.master.controller.req.RackSearchReq;
import dev.gyungmean.newwms.master.domain.*;
import dev.gyungmean.newwms.master.repository.RackRepository;
import dev.gyungmean.newwms.master.service.dto.RackDto;
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
class RackServiceTest {

    @Mock
    private RackRepository rackRepository;

    @InjectMocks
    private RackService rackService;

    @Test
    @DisplayName("랙 생성 성공")
    void create_success() {
        RackCreateReq req = createRackReq("01010101", "STRG01", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01");

        given(rackRepository.existsById(RackId.of("STRG01", "01010101"))).willReturn(false);
        given(rackRepository.save(any(Rack.class))).willAnswer(inv -> inv.getArgument(0));

        RackDto result = rackService.create(req);
        assertThat(result.getRackNo()).isEqualTo("01010101");
        verify(rackRepository).save(any(Rack.class));
    }

    @Test
    @DisplayName("랙 생성 실패 - 중복")
    void create_duplicate() {
        RackCreateReq req = createRackReq("01010101", "STRG01", RackStatus.AVAILABLE, LuggageStatus.EMPTY, "01");

        given(rackRepository.existsById(RackId.of("STRG01", "01010101"))).willReturn(true);

        assertThatThrownBy(() -> rackService.create(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 존재하는");
    }

    @Test
    @DisplayName("랙 조회 성공")
    void findByRackNo_success() {
        given(rackRepository.findById(RackId.of("STRG01", "01010101"))).willReturn(Optional.of(createRack("01010101")));

        RackDto result = rackService.findByRackNo("STRG01", "01010101");
        assertThat(result.getRackNo()).isEqualTo("01010101");
    }

    @Test
    @DisplayName("랙 조회 실패 - 존재하지 않음")
    void findByRackNo_notFound() {
        given(rackRepository.findById(RackId.of("STRG01", "NOTEXIST"))).willReturn(Optional.empty());

        assertThatThrownBy(() -> rackService.findByRackNo("STRG01", "NOTEXIST"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("검색 - storageId + status 복합 조건")
    void search_byStorageIdAndStatus() {
        given(rackRepository.findByStorageIdAndStatus("STRG01", RackStatus.AVAILABLE))
                .willReturn(List.of(createRack("01010101")));

        RackSearchReq req = new RackSearchReq();
        req.setStorageId("STRG01");
        req.setStatus(RackStatus.AVAILABLE);

        assertThat(rackService.search(req)).hasSize(1);
    }

    @Test
    @DisplayName("검색 - storageId만")
    void search_byStorageIdOnly() {
        given(rackRepository.findByStorageId("STRG01"))
                .willReturn(List.of(createRack("01010101"), createRack("01020101")));

        RackSearchReq req = new RackSearchReq();
        req.setStorageId("STRG01");

        assertThat(rackService.search(req)).hasSize(2);
    }

    @Test
    @DisplayName("검색 - 조건 없으면 전체 조회")
    void search_noCondition() {
        given(rackRepository.findAll()).willReturn(List.of(createRack("01010101")));

        assertThat(rackService.search(new RackSearchReq())).hasSize(1);
    }

    @Test
    @DisplayName("랙 삭제 성공")
    void delete_success() {
        given(rackRepository.existsById(RackId.of("STRG01", "01010101"))).willReturn(true);

        rackService.delete("STRG01", "01010101");
        verify(rackRepository).deleteById(RackId.of("STRG01", "01010101"));
    }

    @Test
    @DisplayName("랙 삭제 실패 - 존재하지 않음")
    void delete_notFound() {
        given(rackRepository.existsById(RackId.of("STRG01", "NOTEXIST"))).willReturn(false);

        assertThatThrownBy(() -> rackService.delete("STRG01", "NOTEXIST"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("파트너 랙 조회 성공")
    void findPartner_success() {
        given(rackRepository.findById(RackId.of("STRG01", "01010101"))).willReturn(Optional.of(createRack("01010101")));
        given(rackRepository.findById(RackId.of("STRG01", "01020101"))).willReturn(Optional.of(createRack("01020101")));

        RackDto result = rackService.findPartner("STRG01", "01010101");
        assertThat(result.getRackNo()).isEqualTo("01020101");
    }

    @Test
    @DisplayName("파트너 랙 조회 실패 - 원본 랙 없음")
    void findPartner_rackNotFound() {
        given(rackRepository.findById(RackId.of("STRG01", "NOTEXIST"))).willReturn(Optional.empty());

        assertThatThrownBy(() -> rackService.findPartner("STRG01", "NOTEXIST"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("파트너 랙 조회 실패 - 파트너 랙 없음")
    void findPartner_partnerNotFound() {
        given(rackRepository.findById(RackId.of("STRG01", "01010101"))).willReturn(Optional.of(createRack("01010101")));
        given(rackRepository.findById(RackId.of("STRG01", "01020101"))).willReturn(Optional.empty());

        assertThatThrownBy(() -> rackService.findPartner("STRG01", "01010101"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("파트너 랙을 찾을 수 없습니다");
    }

    private Rack createRack(String rackNo) {
        return Rack.builder().rackNo(rackNo).storageId("STRG01")
                .status(RackStatus.AVAILABLE).lugg(LuggageStatus.EMPTY).zoneCode("01").build();
    }

    private RackCreateReq createRackReq(String rackNo, String storageId,
                                         RackStatus status, LuggageStatus lugg, String zoneCode) {
        try {
            RackCreateReq req = new RackCreateReq();
            setField(req, "rackNo", rackNo);
            setField(req, "storageId", storageId);
            setField(req, "status", status);
            setField(req, "lugg", lugg);
            setField(req, "zoneCode", zoneCode);
            return req;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setField(Object obj, String fieldName, Object value) throws Exception {
        var field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }
}

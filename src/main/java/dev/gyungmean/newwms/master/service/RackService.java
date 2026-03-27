package dev.gyungmean.newwms.master.service;

import dev.gyungmean.newwms.master.controller.req.RackCreateReq;
import dev.gyungmean.newwms.master.controller.req.RackSearchReq;
import dev.gyungmean.newwms.master.domain.Rack;
import dev.gyungmean.newwms.master.domain.RackId;
import dev.gyungmean.newwms.master.domain.RackStatus;
import dev.gyungmean.newwms.master.repository.RackRepository;
import dev.gyungmean.newwms.master.service.dto.RackDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RackService {

    private final RackRepository rackRepository;

    @Transactional
    public RackDto create(RackCreateReq req) {
        RackId id = RackId.of(req.getStorageId(), req.getRackNo());
        if (rackRepository.existsById(id)) {
            throw new IllegalArgumentException("이미 존재하는 랙입니다: " + req.getStorageId() + "/" + req.getRackNo());
        }
        Rack rack = Rack.builder()
                .rackNo(req.getRackNo())
                .storageId(req.getStorageId())
                .status(req.getStatus())
                .lugg(req.getLugg())
                .zoneCode(req.getZoneCode())
                .build();
        return RackDto.from(rackRepository.save(rack));
    }

    public RackDto findByRackNo(String storageId, String rackNo) {
        Rack rack = rackRepository.findById(RackId.of(storageId, rackNo))
                .orElseThrow(() -> new IllegalArgumentException("랙을 찾을 수 없습니다: " + storageId + "/" + rackNo));
        return RackDto.from(rack);
    }

    public List<RackDto> search(RackSearchReq req) {
        List<Rack> racks;
        if (req.getStorageId() != null && req.getStatus() != null) {
            racks = rackRepository.findByStorageIdAndStatus(req.getStorageId(), req.getStatus());
        } else if (req.getStorageId() != null) {
            racks = rackRepository.findByStorageId(req.getStorageId());
        } else if (req.getStatus() != null) {
            racks = rackRepository.findByStatus(req.getStatus());
        } else if (req.getZoneCode() != null) {
            racks = rackRepository.findByZoneCode(req.getZoneCode());
        } else {
            racks = rackRepository.findAll();
        }
        return racks.stream().map(RackDto::from).toList();
    }

    @Transactional
    public void delete(String storageId, String rackNo) {
        RackId id = RackId.of(storageId, rackNo);
        if (!rackRepository.existsById(id)) {
            throw new IllegalArgumentException("랙을 찾을 수 없습니다: " + storageId + "/" + rackNo);
        }
        rackRepository.deleteById(id);
    }

    public RackDto findPartner(String storageId, String rackNo) {
        Rack rack = rackRepository.findById(RackId.of(storageId, rackNo))
                .orElseThrow(() -> new IllegalArgumentException("랙을 찾을 수 없습니다: " + storageId + "/" + rackNo));
        String partnerRackNo = rack.getPartnerRackNo();
        if (partnerRackNo == null || partnerRackNo.isBlank()) {
            throw new IllegalArgumentException("파트너 랙이 설정되지 않았습니다: " + rackNo);
        }
        Rack partner = rackRepository.findById(RackId.of(storageId, partnerRackNo))
                .orElseThrow(() -> new IllegalArgumentException("파트너 랙을 찾을 수 없습니다: " + partnerRackNo));
        return RackDto.from(partner);
    }
}

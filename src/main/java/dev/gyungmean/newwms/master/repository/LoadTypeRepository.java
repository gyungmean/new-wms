package dev.gyungmean.newwms.master.repository;

import dev.gyungmean.newwms.master.domain.BagType;
import dev.gyungmean.newwms.master.domain.LoadType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoadTypeRepository extends JpaRepository<LoadType, String> {

    List<LoadType> findByBagType(BagType bagType);

    List<LoadType> findByUsageYn(String usageYn);
}

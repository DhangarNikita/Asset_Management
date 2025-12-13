package com.asset.AssetManagement.repository;

import com.asset.AssetManagement.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset,Long> {
    boolean existsBySerialName(String serialName);
    List<Asset> findByExpireDateBefore(java.time.LocalDate date);
}

package com.asset.AssetManagement.repository;

import com.asset.AssetManagement.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset,Long> {
    boolean existsBySerialName(String serialName);
}

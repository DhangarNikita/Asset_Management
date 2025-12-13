package com.asset.AssetManagement.repository;

import com.asset.AssetManagement.entity.ArchivedAsset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchivedAssetRepository extends JpaRepository<ArchivedAsset,Long> {
}

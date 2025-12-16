package com.asset.AssetManagement.service.Impl;

import com.asset.AssetManagement.entity.ArchivedAsset;
import com.asset.AssetManagement.entity.Asset;
import com.asset.AssetManagement.repository.ArchivedAssetRepository;
import com.asset.AssetManagement.repository.AssetRepository;
import com.asset.AssetManagement.service.ExpiredAssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpiredAssetServiceImpl implements ExpiredAssetService {

    private final AssetRepository assetRepository;
    private final ArchivedAssetRepository archivedAssetRepository;

    @Autowired
    public ExpiredAssetServiceImpl(AssetRepository assetRepository, ArchivedAssetRepository archivedAssetRepository) {
        this.assetRepository = assetRepository;
        this.archivedAssetRepository = archivedAssetRepository;
    }

    @Transactional
    @Override
    public void moveExpiredAssets() {
        List<Asset> expiredAssets = assetRepository.findByExpireDateBefore(LocalDate.now());
        for (Asset asset : expiredAssets) {
            ArchivedAsset archived = new ArchivedAsset();
            archived.setModelName(asset.getModelName());
            archived.setSerialName(asset.getSerialName());
            archived.setManufactureDate(asset.getManufactureDate());
            archived.setExpireDate(asset.getExpireDate());
            archived.setPurchaseDate(asset.getPurchaseDate());
            archived.setStatus(asset.getStatus());
            archived.setType(asset.getType());
            archivedAssetRepository.save(archived);
        }
        assetRepository.deleteAll(expiredAssets);
    }
}

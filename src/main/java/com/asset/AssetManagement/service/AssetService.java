package com.asset.AssetManagement.service;

import com.asset.AssetManagement.dto.AssetRequestDto;
import com.asset.AssetManagement.dto.AssetResponseDto;
import com.asset.AssetManagement.dto.AssetUpdateDto;
import org.springframework.data.domain.Page;

public interface AssetService {
    AssetResponseDto createAsset(AssetRequestDto dto);
    AssetResponseDto getAsset(Long id);
    //List<AssetResponseDto> getAllAsset();
    Page<AssetResponseDto> getAllAsset(int page, int size);
    void delete(Long id);
    AssetResponseDto updateAsset(Long id, AssetUpdateDto dto);
    AssetResponseDto assignAsset(Long assetId, Long employeeId);

}

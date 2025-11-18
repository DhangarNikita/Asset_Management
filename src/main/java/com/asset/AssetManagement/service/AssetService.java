package com.asset.AssetManagement.service;

import com.asset.AssetManagement.dto.AssetRequestDto;
import com.asset.AssetManagement.dto.AssetResponseDto;

import java.util.List;

public interface AssetService {
    AssetResponseDto createAsset(AssetRequestDto dto);
    AssetResponseDto getAsset(Long id);
    List<AssetResponseDto> getAllAsset();
    void delete(Long id);
    AssetResponseDto updateAsset(Long id, AssetRequestDto dto);

}

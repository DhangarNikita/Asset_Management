package com.asset.AssetManagement.controller;

import com.asset.AssetManagement.dto.AssetRequestDto;
import com.asset.AssetManagement.dto.AssetResponseDto;
import com.asset.AssetManagement.dto.AssetUpdateDto;
import com.asset.AssetManagement.dto.AssignAssetRequestDto;
import com.asset.AssetManagement.service.AssetService;
import com.asset.AssetManagement.util.ValidatorUtil;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asset")
public class AssetController {
    public final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    public ResponseEntity<AssetResponseDto> createAsset(@Valid @RequestBody AssetRequestDto dto) {
        ValidatorUtil.serialValid(dto.getSerialName());
        ValidatorUtil.validateDate(dto.getPurchaseDate(),dto.getExpireDate(),dto.getManufactureDate());
        return ResponseEntity.ok(assetService.createAsset(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetResponseDto> getAssetById(@PathVariable Long id){
        return ResponseEntity.ok(assetService.getAsset(id));
    }

    @GetMapping
    public ResponseEntity<Page<AssetResponseDto>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(assetService.getAllAsset(page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        ValidatorUtil.validateAssetId(id);
        assetService.delete(id);
        return ResponseEntity.ok("Asset deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetResponseDto> updateAsset(
            @PathVariable Long id,
            @Valid @RequestBody AssetUpdateDto dto) {
        ValidatorUtil.validateAssetId(id);
        ValidatorUtil.validateDate(dto.getPurchaseDate(),dto.getExpireDate(),dto.getManufactureDate());
        ValidatorUtil.validateDuplicateSerial(dto.getSerialName(), id);
        return ResponseEntity.ok(assetService.updateAsset(id, dto));
    }

    @PutMapping("/assign/{assetId}")
    public ResponseEntity<AssetResponseDto> assignAsset(@PathVariable Long assetId, @Valid @RequestBody AssignAssetRequestDto requestDto)
    {
        ValidatorUtil.validateAssetId(assetId);
        ValidatorUtil.validateEmployeeId(requestDto.getEmployeeId());
        return ResponseEntity.ok(assetService.assignAsset(assetId, requestDto.getEmployeeId()));
    }
}
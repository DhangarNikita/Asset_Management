package com.asset.AssetManagement.controller;

import com.asset.AssetManagement.dto.AssetRequestDto;
import com.asset.AssetManagement.dto.AssetResponseDto;
import com.asset.AssetManagement.dto.AssetUpdateDto;
import com.asset.AssetManagement.dto.AssignAssetRequestDto;
import com.asset.AssetManagement.service.AssetService;
import com.asset.AssetManagement.util.ValidatorUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/asset")
@Tag(name="Asset Management", description="APIs for managing assets")
public class AssetController {
    public final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    @Operation(summary = "Create a new asset")
    public ResponseEntity<AssetResponseDto> createAsset(@Valid @RequestBody AssetRequestDto dto) {
        ValidatorUtil.serialValid(dto.getSerialName());
        ValidatorUtil.validateDate(dto.getPurchaseDate(),dto.getExpireDate(),dto.getManufactureDate());
        return ResponseEntity.status(HttpStatus.CREATED).body(assetService.createAsset(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get asset by ID")
    public ResponseEntity<AssetResponseDto> getAssetById(@PathVariable Long id){
        return ResponseEntity.ok(assetService.getAsset(id));
    }

    @GetMapping
    @Operation(summary = "Get all assets with pagination")
    public ResponseEntity<Page<AssetResponseDto>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(assetService.getAllAsset(page, size));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete asset by ID")
    public ResponseEntity<String> delete(@PathVariable Long id){
        ValidatorUtil.validateAssetId(id);
        assetService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update asset by ID")
    public ResponseEntity<AssetResponseDto> updateAsset(
            @PathVariable Long id,
            @Valid @RequestBody AssetUpdateDto dto) {
        ValidatorUtil.validateAssetId(id);
        ValidatorUtil.validateDate(dto.getPurchaseDate(),dto.getExpireDate(),dto.getManufactureDate());
        ValidatorUtil.validateDuplicateSerial(dto.getSerialName(), id);
        return ResponseEntity.ok(assetService.updateAsset(id, dto));
    }

    @PutMapping("/assign/{assetId}")
    @Operation(summary = "Assign asset to an employee")
    public ResponseEntity<AssetResponseDto> assignAsset(@PathVariable Long assetId, @Valid @RequestBody AssignAssetRequestDto requestDto)
    {
        ValidatorUtil.validateAssetId(assetId);
        ValidatorUtil.validateEmployeeId(requestDto.getEmployeeId());
        return ResponseEntity.ok(assetService.assignAsset(assetId, requestDto.getEmployeeId()));
    }
}
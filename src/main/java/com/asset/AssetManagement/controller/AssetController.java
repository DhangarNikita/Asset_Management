package com.asset.AssetManagement.controller;

import com.asset.AssetManagement.dto.AssetRequestDto;
import com.asset.AssetManagement.dto.AssetResponseDto;
import com.asset.AssetManagement.service.AssetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

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
        return ResponseEntity.ok(assetService.createAsset(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetResponseDto> getAssetById(@PathVariable Long id){
        return ResponseEntity.ok(assetService.getAsset(id));

    }

    @GetMapping
    public ResponseEntity<List<AssetResponseDto>> getAll(){
        return ResponseEntity.ok(assetService.getAllAsset());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        assetService.delete(id);
        return ResponseEntity.ok("Asset deleted successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetResponseDto> updateAsset(@PathVariable Long id, @Valid @RequestBody AssetRequestDto dto) {
        return ResponseEntity.ok(assetService.updateAsset(id, dto));
    }
}
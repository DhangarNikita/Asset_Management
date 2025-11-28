package com.asset.AssetManagement.controller;

import com.asset.AssetManagement.dto.AssetRequestDto;
import com.asset.AssetManagement.dto.AssetResponseDto;
import com.asset.AssetManagement.dto.AssetUpdateDto;
import com.asset.AssetManagement.dto.AssignAssetRequestDto;
import com.asset.AssetManagement.enums.AssetStatus;
import com.asset.AssetManagement.enums.AssetType;
import com.asset.AssetManagement.exception.ResourceNotFoundException;
import com.asset.AssetManagement.service.AssetService;
import com.asset.AssetManagement.util.ValidatorUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssetControllerTest {

    @Mock
    private AssetService assetService;
    @InjectMocks
    private AssetController assetController;

    @Test
    void testCreateAsset() {
        AssetRequestDto dto = AssetRequestDto.builder()
                .modelName("ModelX")
                .serialName("SN123")
                .manufactureDate(LocalDate.now())
                .expireDate(LocalDate.now().plusYears(1))
                .purchaseDate(LocalDate.now().minusDays(10))
                .assignTo("Nikita Dhangar")
                .status(AssetStatus.ACTIVE)
                .type(AssetType.LAPTOP)
                .build();
        AssetResponseDto response = new AssetResponseDto();
        when(assetService.createAsset(dto)).thenReturn(response);
        try (MockedStatic<ValidatorUtil> util = mockStatic(ValidatorUtil.class)) {
            ResponseEntity<AssetResponseDto> result = assetController.createAsset(dto);
            util.verify(() -> ValidatorUtil.serialValid("SN123"));
            util.verify(() -> ValidatorUtil.validateDate(any(), any(), any()));
            assertEquals(response, result.getBody());
            assertEquals(response, result.getBody());
        }
    }

    @Test
    void testGetAssetById() {
        Long assetId = 1L;
        AssetResponseDto response = new AssetResponseDto();
        when(assetService.getAsset(assetId)).thenReturn(response);
        ResponseEntity<AssetResponseDto> result = assetController.getAssetById(assetId);
        assertEquals(response, result.getBody());
    }

    @Test
    void testGetAssetByIdNotFound() {
        Long assetId = 2L;
        when(assetService.getAsset(assetId)).thenThrow(new ResourceNotFoundException("Asset not found"));
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> assetController.getAssetById(assetId));
        assertEquals("Asset not found", ex.getMessage());
    }

    @Test
    void testGetAllAssets() {
        Page<AssetResponseDto> page = new PageImpl<>(List.of(new AssetResponseDto()));
        when(assetService.getAllAsset(0, 10)).thenReturn(page);
        ResponseEntity<Page<AssetResponseDto>> result = assetController.getAll(0, 10);
        assertEquals(1, result.getBody().getTotalElements());
    }

    @Test
    void testDeleteAsset() {
        Long assetId = 1L;
        try (MockedStatic<ValidatorUtil> util = mockStatic(ValidatorUtil.class)) {
            ResponseEntity<String> response = assetController.delete(assetId);
            util.verify(() -> ValidatorUtil.validateAssetId(assetId));
            verify(assetService).delete(assetId);
            assertEquals("Asset deleted successfully", response.getBody());
        }
    }
    
    @Test
    void testDeleteAssetNotFound() {
        Long assetId = 2L;
        try (MockedStatic<ValidatorUtil> util = mockStatic(ValidatorUtil.class)) {
            util.when(() -> ValidatorUtil.validateAssetId(assetId))
                .thenThrow(new ResourceNotFoundException("Asset ID not found: " + assetId));
            ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> assetController.delete(assetId));
            assertEquals("Asset ID not found: " + assetId, ex.getMessage());
        }
    }
    
    @Test
    void testUpdateAsset(){
        Long assetId = 1L;
        AssetUpdateDto dto = new AssetUpdateDto();
        AssetResponseDto responseDto = new AssetResponseDto();
        when(assetService.updateAsset(assetId, dto)).thenReturn(responseDto);
        try (MockedStatic<ValidatorUtil> util = mockStatic(ValidatorUtil.class)) {
            ResponseEntity<AssetResponseDto> response = assetController.updateAsset(assetId, dto);
            util.verify(() -> ValidatorUtil.validateAssetId(assetId));
            util.verify(() -> ValidatorUtil.validateDate(any(), any(), any()));
            util.verify(() -> ValidatorUtil.validateDuplicateSerial(dto.getSerialName(), assetId));
            assertEquals(responseDto, response.getBody());
        }
    }

    @Test
    void testUpdateAssetNotFound() {
        Long assetId = 20L;
        AssetUpdateDto dto = new AssetUpdateDto();
        try (MockedStatic<ValidatorUtil> util = mockStatic(ValidatorUtil.class)) {
            util.when(() -> ValidatorUtil.validateAssetId(assetId))
                .thenThrow(new ResourceNotFoundException("Asset ID not found: " + assetId));
            ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> assetController.updateAsset(assetId, dto));
            assertEquals("Asset ID not found: " + assetId, ex.getMessage());
        }
    }

    @Test
    void testAssignAsset() {
        Long assetId = 1L;
        Long employeeId = 100L;

        AssignAssetRequestDto requestDto = new AssignAssetRequestDto();
        requestDto.setEmployeeId(employeeId);
        AssetResponseDto dto = new AssetResponseDto();
        when(assetService.assignAsset(assetId, employeeId)).thenReturn(dto);
        try (MockedStatic<ValidatorUtil> util = mockStatic(ValidatorUtil.class)) {
            ResponseEntity<AssetResponseDto> response = assetController.assignAsset(assetId, requestDto);
            util.verify(() -> ValidatorUtil.validateAssetId(assetId));
            assertEquals(dto, response.getBody());
        }
    }

    @Test
    void testAssignAssetNotFound() {
        Long assetId = 2L;
        Long employeeId = 200L;

        AssignAssetRequestDto requestDto = new AssignAssetRequestDto();
        requestDto.setEmployeeId(employeeId);
        try (MockedStatic<ValidatorUtil> util = mockStatic(ValidatorUtil.class)) {
            util.when(() -> ValidatorUtil.validateAssetId(assetId))
                .thenThrow(new ResourceNotFoundException("Asset ID not found: " + assetId));
            ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> assetController.assignAsset(assetId, requestDto));
            assertEquals("Asset ID not found: " + assetId, ex.getMessage());
        }
    }
}


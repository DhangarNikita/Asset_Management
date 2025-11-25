package com.asset.AssetManagement.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.asset.AssetManagement.dto.AssetRequestDto;
import com.asset.AssetManagement.dto.AssetResponseDto;
import com.asset.AssetManagement.dto.AssetUpdateDto;
import com.asset.AssetManagement.entity.Asset;
import com.asset.AssetManagement.entity.Employee;
import com.asset.AssetManagement.enums.AssetStatus;
import com.asset.AssetManagement.enums.AssetType;
import com.asset.AssetManagement.repository.AssetRepository;
import com.asset.AssetManagement.repository.EmployeeRepository;
import com.asset.AssetManagement.service.Impl.AssetServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class AssetServiceImplTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AssetServiceImpl assetService;

    @Test
    void testCreateAsset_Success() {
        AssetRequestDto dto = new AssetRequestDto();
        dto.setModelName("ModelX");
        dto.setSerialName("SN123");
        dto.setManufactureDate(java.time.LocalDate.now());
        dto.setExpireDate(java.time.LocalDate.now().plusYears(1));
        dto.setPurchaseDate(java.time.LocalDate.now().minusDays(10));
        dto.setAssignTo("John Doe");
        dto.setStatus(AssetStatus.ACTIVE);
        dto.setType(AssetType.LAPTOP);
        dto.setEmployeeId(1L);

        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setEmployeeName("John Doe");

        Asset asset = Asset.builder()
                .modelName(dto.getModelName())
                .serialName(dto.getSerialName())
                .manufactureDate(dto.getManufactureDate())
                .expireDate(dto.getExpireDate())
                .purchaseDate(dto.getPurchaseDate())
                .assignTo(dto.getAssignTo())
                .status(dto.getStatus())
                .type(dto.getType())
                .employee(employee)
                .build();

        AssetResponseDto responseDto = new AssetResponseDto();
        responseDto.setModelName("ModelX");

        when(employeeRepository.findById(1L)).thenReturn(java.util.Optional.of(employee));
        when(assetRepository.save(any(Asset.class))).thenReturn(asset);
        when(modelMapper.map(any(Asset.class), eq(AssetResponseDto.class))).thenReturn(responseDto);

        AssetResponseDto result = assetService.createAsset(dto);

        assertNotNull(result);
        assertEquals("ModelX", result.getModelName());
        verify(assetRepository).save(any(Asset.class));
    }

    @Test
    void testGetAsset_Success() {
        Long assetId = 1L;
        Asset asset = Asset.builder()
                .modelName("ModelX")
                .serialName("SN123")
                .build();

        AssetResponseDto responseDto = new AssetResponseDto();
        responseDto.setModelName("ModelX");

        when(assetRepository.findById(assetId)).thenReturn(java.util.Optional.of(asset));
        when(modelMapper.map(asset, AssetResponseDto.class)).thenReturn(responseDto);

        AssetResponseDto result = assetService.getAsset(assetId);

        assertNotNull(result);
        assertEquals("ModelX", result.getModelName());
        verify(assetRepository).findById(assetId);
        verify(modelMapper).map(asset, AssetResponseDto.class);
    }

    @Test
    void testGetAllAsset_Success() {
        Asset asset = Asset.builder()
                .modelName("ModelX")
                .serialName("SN123")
                .build();

        AssetResponseDto responseDto = new AssetResponseDto();
        responseDto.setModelName("ModelX");

        List<Asset> assetList = List.of(asset);
        Page<Asset> assetPage = new org.springframework.data.domain.PageImpl<>(assetList);

        when(assetRepository.findAll(any(Pageable.class))).thenReturn(assetPage);
        when(modelMapper.map(asset, AssetResponseDto.class)).thenReturn(responseDto);

        Page<AssetResponseDto> result = assetService.getAllAsset(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("ModelX", result.getContent().get(0).getModelName());
        verify(assetRepository).findAll(any(Pageable.class));
        verify(modelMapper).map(asset, AssetResponseDto.class);
    }

    @Test
    void testDeleteAsset_Success() {
        Long assetId = 1L;

        assetService.delete(assetId);

        verify(assetRepository).deleteById(assetId);
    }

    @Test
    void testUpdateAsset_Success() {
        Long assetId = 1L;
        AssetUpdateDto updateDto = new AssetUpdateDto();
        updateDto.setModelName("UpdatedModel");
        updateDto.setSerialName("UpdatedSN");

        Asset existingAsset = Asset.builder()
                .modelName("OldModel")
                .serialName("OldSN")
                .build();

        Asset updatedAsset = Asset.builder()
                .modelName("UpdatedModel")
                .serialName("UpdatedSN")
                .build();

        AssetResponseDto responseDto = new AssetResponseDto();
        responseDto.setModelName("UpdatedModel");
        responseDto.setSerialName("UpdatedSN");

        when(assetRepository.findById(assetId)).thenReturn(java.util.Optional.of(existingAsset));
        when(assetRepository.save(any(Asset.class))).thenReturn(updatedAsset);
        when(modelMapper.map(updatedAsset, AssetResponseDto.class)).thenReturn(responseDto);

        AssetResponseDto result = assetService.updateAsset(assetId, updateDto);

        assertNotNull(result);
        assertEquals("UpdatedModel", result.getModelName());
        assertEquals("UpdatedSN", result.getSerialName());
        verify(assetRepository).findById(assetId);
        verify(assetRepository).save(existingAsset);
        verify(modelMapper).map(updatedAsset, AssetResponseDto.class);
    }

    @Test
    void testAssignAsset_Success() {
        Long assetId = 1L;
        Long employeeId = 2L;

        Asset asset = Asset.builder()
                .modelName("ModelX")
                .serialName("SN123")
                .build();

        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setEmployeeName("Jane Doe");

        AssetResponseDto responseDto = new AssetResponseDto();
        responseDto.setModelName("ModelX");
        responseDto.setEmployeeId(employeeId);

        when(assetRepository.findById(assetId)).thenReturn(java.util.Optional.of(asset));
        when(employeeRepository.findById(employeeId)).thenReturn(java.util.Optional.of(employee));
        when(assetRepository.save(any(Asset.class))).thenReturn(asset);
        when(modelMapper.map(asset, AssetResponseDto.class)).thenReturn(responseDto);

        AssetResponseDto result = assetService.assignAsset(assetId, employeeId);

        assertNotNull(result);
        assertEquals(employeeId, result.getEmployeeId());
        assertEquals("ModelX", result.getModelName());
        verify(assetRepository).findById(assetId);
        verify(employeeRepository).findById(employeeId);
        verify(assetRepository).save(asset);
        verify(modelMapper).map(asset, AssetResponseDto.class);
    }
}

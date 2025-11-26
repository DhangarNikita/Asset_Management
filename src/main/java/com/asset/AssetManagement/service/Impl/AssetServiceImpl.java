package com.asset.AssetManagement.service.Impl;

import com.asset.AssetManagement.dto.AssetRequestDto;
import com.asset.AssetManagement.dto.AssetResponseDto;
import com.asset.AssetManagement.dto.AssetUpdateDto;
import com.asset.AssetManagement.entity.Asset;
import com.asset.AssetManagement.entity.Employee;
import com.asset.AssetManagement.exception.ResourceNotFoundException;
import com.asset.AssetManagement.repository.AssetRepository;
import com.asset.AssetManagement.repository.EmployeeRepository;
import com.asset.AssetManagement.service.AssetService;
import com.asset.AssetManagement.util.ValidatorUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AssetServiceImpl implements AssetService {
    private final ModelMapper modelMapper;
    private final AssetRepository assetRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public AssetServiceImpl(AssetRepository assetRepository, EmployeeRepository employeeRepository,ModelMapper modelMapper) {
        this.assetRepository = assetRepository;
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    public AssetResponseDto createAsset(AssetRequestDto dto) {
        if(dto.getAssignTo() == null){
            dto.setAssignTo("Unassigned");
        }
        Asset asset = Asset.builder()
                .modelName(dto.getModelName())
                .serialName(dto.getSerialName())
                .manufactureDate(dto.getManufactureDate())
                .expireDate(dto.getExpireDate())
                .purchaseDate(dto.getPurchaseDate())
                .assignTo(dto.getAssignTo())
                .status(dto.getStatus())
                .type(dto.getType())
                .build();
        assetRepository.save(asset);
        return modelMapper.map(asset, AssetResponseDto.class);
    }

    public AssetResponseDto getAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
        return modelMapper.map(asset, AssetResponseDto.class);
    }

    public Page<AssetResponseDto>getAllAsset(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Asset> assetPage = assetRepository.findAll(pageable);
        return assetPage.map(asset-> modelMapper.map(asset,AssetResponseDto.class));
    }

    public void delete(Long id) {
        assetRepository.deleteById(id);
    }

    public AssetResponseDto updateAsset(Long id, AssetUpdateDto dto) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
        ValidatorUtil.updateIfValid(dto.getSerialName(), asset::setSerialName);
        ValidatorUtil.updateIfValid(dto.getModelName(), asset::setModelName);
        ValidatorUtil.updateIfPresent(dto.getManufactureDate(), asset::setManufactureDate);
        ValidatorUtil.updateIfPresent(dto.getPurchaseDate(), asset::setPurchaseDate);
        ValidatorUtil.updateIfPresent(dto.getExpireDate(), asset::setExpireDate);
        ValidatorUtil.updateIfPresent(dto.getStatus(), asset::setStatus);
        ValidatorUtil.updateIfPresent(dto.getType(), asset::setType);
        assetRepository.save(asset);
        return modelMapper.map(asset, AssetResponseDto.class);
    }

    public AssetResponseDto assignAsset(Long assetId, Long employeeId) {
        Asset asset = assetRepository.findById(assetId).get();
        Employee employee = employeeRepository.findById(employeeId).get();
        asset.setEmployee(employee);
        asset.setAssignTo(employee.getEmployeeName());
        assetRepository.save(asset);
        AssetResponseDto dto = modelMapper.map(asset, AssetResponseDto.class);
        dto.setEmployeeId(employee.getEmployeeId());
        return dto;
    }
}

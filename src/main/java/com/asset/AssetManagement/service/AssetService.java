package com.asset.AssetManagement.service;

import com.asset.AssetManagement.dto.AssetRequestDto;
import com.asset.AssetManagement.dto.AssetResponseDto;
import com.asset.AssetManagement.entity.Asset;
import com.asset.AssetManagement.entity.Employee;
import com.asset.AssetManagement.repository.AssetRepository;
import com.asset.AssetManagement.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssetService {
    @Autowired
    private ModelMapper modelMapper;

    private final AssetRepository assetRepository;
    private final EmployeeRepository employeeRepository;

    public AssetService(AssetRepository assetRepository, EmployeeRepository employeeRepository) {
        this.assetRepository = assetRepository;
        this.employeeRepository = employeeRepository;
    }

    public AssetResponseDto createAsset(AssetRequestDto dto) {
        Asset.AssetBuilder assetBuilder = Asset.builder()
                .modelName(dto.getModelName())
                .serialName(dto.getSerialName())
                .manufactureDate(dto.getManufactureDate())
                .expireDate(dto.getExpireDate())
                .purchaseDate(dto.getPurchaseDate())
                .cost(dto.getCost())
                .status(dto.getStatus())
                .type(dto.getType());
        if (dto.getEmployeeId() != null) {
            Employee emp = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            assetBuilder.employee(emp);
        }
        Asset asset = assetBuilder.build();
        assetRepository.save(asset);
        return modelMapper.map(asset, AssetResponseDto.class);
    }


    public AssetResponseDto getAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
        return modelMapper.map(asset, AssetResponseDto.class);
    }

    public List<AssetResponseDto> getAllAsset() {
        List<Asset> list = assetRepository.findAll();
        List<AssetResponseDto> asset = new ArrayList<>();
        for (Asset asset1 : list) {
            AssetResponseDto dto = modelMapper.map(asset1, AssetResponseDto.class);
            asset.add(dto);
        }
        return asset;
    }

    public void delete(Long id) {
        if (!assetRepository.existsById(id)) {
            throw new RuntimeException();
        }
        assetRepository.deleteById(id);
    }


    public AssetResponseDto updateAsset(Long id, AssetRequestDto dto) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found"));
        if (dto.getModelName() != null) asset.setModelName(dto.getModelName());
        if (dto.getSerialName() != null) asset.setSerialName(dto.getSerialName());
        if (dto.getManufactureDate() != null) asset.setManufactureDate(dto.getManufactureDate());
        if (dto.getExpireDate() != null) asset.setExpireDate(dto.getExpireDate());
        if (dto.getPurchaseDate() != null) asset.setPurchaseDate(dto.getPurchaseDate());
        if (dto.getCost() != null) asset.setCost(dto.getCost());
        if (dto.getStatus() != null) asset.setStatus(dto.getStatus());
        if (dto.getType() != null) asset.setType(dto.getType());
        if (dto.getEmployeeId() != null) {
            Employee emp = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            asset.setEmployee(emp);
        }
        assetRepository.save(asset);
        AssetResponseDto responseDto = modelMapper.map(asset, AssetResponseDto.class);
        if (asset.getEmployee() != null) {
            responseDto.setEmployeeId(asset.getEmployee().getEmployeeId());
        }
        return responseDto;
    }
}

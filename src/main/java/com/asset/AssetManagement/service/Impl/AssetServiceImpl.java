package com.asset.AssetManagement.service.Impl;

import com.asset.AssetManagement.dto.AssetRequestDto;
import com.asset.AssetManagement.dto.AssetResponseDto;
import com.asset.AssetManagement.entity.Asset;
import com.asset.AssetManagement.entity.Employee;
import com.asset.AssetManagement.exception.DuplicateAssetException;
import com.asset.AssetManagement.exception.InvalidAssetDateException;
import com.asset.AssetManagement.exception.ResourceNotFoundException;
import com.asset.AssetManagement.repository.AssetRepository;
import com.asset.AssetManagement.repository.EmployeeRepository;
import com.asset.AssetManagement.service.AssetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {
    @Autowired
    private ModelMapper modelMapper;

    private final AssetRepository assetRepository;
    private final EmployeeRepository employeeRepository;

    public AssetServiceImpl(AssetRepository assetRepository, EmployeeRepository employeeRepository) {
        this.assetRepository = assetRepository;
        this.employeeRepository = employeeRepository;
    }

    public AssetResponseDto createAsset(AssetRequestDto dto) {

       if(assetRepository.existsBySerialName((dto.getSerialName()))){
           throw new DuplicateAssetException("This serial number asset is already exist");
       }

       if(dto.getExpireDate().isBefore(dto.getPurchaseDate())){
           throw new InvalidAssetDateException("Expire date cannot be before purchase date");
       }
        Asset.AssetBuilder assetBuilder = Asset.builder()
                .modelName(dto.getModelName())
                .serialName(dto.getSerialName())
                .manufactureDate(dto.getManufactureDate())
                .expireDate(dto.getExpireDate())
                .purchaseDate(dto.getPurchaseDate())
                .assignTo(dto.getAssignTo())
                .status(dto.getStatus())
                .type(dto.getType());

        if (dto.getEmployeeId() != null) {
            Employee emp = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
            assetBuilder.employee(emp);
        }
        Asset asset = assetBuilder.build();
        assetRepository.save(asset);
        return modelMapper.map(asset, AssetResponseDto.class);
    }


    public AssetResponseDto getAsset(Long id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));
        return modelMapper.map(asset, AssetResponseDto.class);
    }

    public List<AssetResponseDto> getAllAsset(){
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
            throw new ResourceNotFoundException("Id not present");
        }
        assetRepository.deleteById(id);
    }

    public AssetResponseDto updateAsset(Long id, AssetRequestDto dto) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found"));

        if(dto.getSerialName()!= null && assetRepository.existsBySerialName(dto.getSerialName()) && !dto.getSerialName().equals(asset.getSerialName()))
        {
            throw new DuplicateAssetException("Serial number already exists: "+dto.getSerialName());
        }

        if (dto.getPurchaseDate() != null && dto.getExpireDate() != null) {
            if (dto.getExpireDate().isBefore(dto.getPurchaseDate()))
            {
                throw new InvalidAssetDateException("Expiry date cannot be before purchase date");
            }
        }

        if (dto.getPurchaseDate() != null && dto.getPurchaseDate().isAfter(LocalDate.now()))
        {
            throw new InvalidAssetDateException("Purchase date cannot be in future");
        }

        if (dto.getManufactureDate() != null &&
                dto.getManufactureDate().isAfter(LocalDate.now()))
        {
            throw new InvalidAssetDateException("Manufacture date cannot be in future");
        }

        if (dto.getExpireDate() != null) asset.setExpireDate(dto.getExpireDate());
        if (dto.getPurchaseDate() != null) asset.setPurchaseDate(dto.getPurchaseDate());
        if (dto.getSerialName() != null) asset.setSerialName(dto.getSerialName());
        if (dto.getManufactureDate() != null) asset.setManufactureDate(dto.getManufactureDate());
        if (dto.getModelName() != null) asset.setModelName(dto.getModelName());
        if (dto.getExpireDate() != null) asset.setExpireDate(dto.getExpireDate());
        if (dto.getAssignTo() != null) asset.setAssignTo(dto.getAssignTo());
        if (dto.getStatus() != null) asset.setStatus(dto.getStatus());
        if (dto.getType() != null) asset.setType(dto.getType());

        if (dto.getEmployeeId() != null) {
            Employee emp = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
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

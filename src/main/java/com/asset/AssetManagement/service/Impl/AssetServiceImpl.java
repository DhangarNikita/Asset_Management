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

/*    public List<AssetResponseDto> getAllAsset(){
        List<Asset> list = assetRepository.findAll();
        List<AssetResponseDto> asset = new ArrayList<>();
        for (Asset asset1 : list) {
            AssetResponseDto dto = modelMapper.map(asset1, AssetResponseDto.class);
            asset.add(dto);
        }
        return asset;
    }*/

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

        if (ValidatorUtil.isValue(dto.getSerialName()))asset.setSerialName(dto.getSerialName());
        if (dto.getManufactureDate() != null) asset.setManufactureDate(dto.getManufactureDate());
        if (dto.getPurchaseDate() != null) asset.setPurchaseDate(dto.getPurchaseDate());
        if (dto.getExpireDate() != null) asset.setExpireDate(dto.getExpireDate());
        if (ValidatorUtil.isValue((dto.getModelName()))) asset.setModelName(dto.getModelName());
        if (dto.getStatus() != null) asset.setStatus(dto.getStatus());
        if (dto.getType() != null) asset.setType(dto.getType());
        assetRepository.save(asset);

       /* AssetResponseDto responseDto = modelMapper.map(asset, AssetResponseDto.class);
        if (asset.getEmployee() != null) {
            responseDto.setEmployeeId(asset.getEmployee().getEmployeeId());
        }*/
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

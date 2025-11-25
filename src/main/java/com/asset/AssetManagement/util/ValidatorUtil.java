package com.asset.AssetManagement.util;

import com.asset.AssetManagement.entity.Asset;
import com.asset.AssetManagement.exception.DuplicateAssetException;
import com.asset.AssetManagement.exception.InvalidAssetDateException;
import com.asset.AssetManagement.exception.ResourceNotFoundException;
import com.asset.AssetManagement.repository.AssetRepository;
import com.asset.AssetManagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ValidatorUtil {

    private static EmployeeRepository employeeRepository;
    private static AssetRepository assetRepository;

    @Autowired
    public ValidatorUtil(EmployeeRepository empRepo, AssetRepository assetRepo){
        ValidatorUtil.employeeRepository = empRepo;
        ValidatorUtil.assetRepository = assetRepo;
    }

    public static void validateAssetId(Long id){
        if(!assetRepository.existsById(id)){
            throw new ResourceNotFoundException("Asset ID not found: " + id);
        }
    }

    public static void validateEmployeeId(Long id){
        if(!employeeRepository.existsById(id)){
            throw new ResourceNotFoundException("Employee ID not found: " + id);
        }
    }

    public static void validateDuplicateSerial(String serialName, Long currentAssetId) {
        if (serialName != null) {
            boolean isDuplicate = assetRepository.existsBySerialName(serialName);

            if (isDuplicate) {
                Asset current = assetRepository.findById(currentAssetId).orElse(null);

                if (current != null && !serialName.equals(current.getSerialName())) {
                    throw new DuplicateAssetException("Serial number already exists: " + serialName);
                }
            }
        }
    }

    public static void validateDate(LocalDate purchaseDate,LocalDate expireDate,LocalDate manufactureDate){
        if(purchaseDate != null && expireDate!=null ){
            if(expireDate.isBefore(purchaseDate)){
                throw new InvalidAssetDateException("Expiry date cannot be before purchase date");
            }
        }
        if(purchaseDate!=null && purchaseDate.isAfter(LocalDate.now())){
            throw new InvalidAssetDateException("Purchase date cannot be in future");
        }
        if(manufactureDate!= null && manufactureDate.isAfter(LocalDate.now())){
            throw new InvalidAssetDateException("Manufacture date cannot be in future");
        }
    }

    public static void serialValid(String serialNumber){
        if(assetRepository.existsBySerialName(serialNumber)){
            throw new DuplicateAssetException("This serial number asset is already exist");
        }
    }

    public static boolean isValue(String str){
        return str!=null && !str.trim().isEmpty();
    }
}
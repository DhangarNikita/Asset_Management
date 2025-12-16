package com.asset.AssetManagement.util;

import com.asset.AssetManagement.constants.Constants;
import com.asset.AssetManagement.entity.Asset;
import com.asset.AssetManagement.exception.DuplicateAssetException;
import com.asset.AssetManagement.exception.InvalidAssetDateException;
import com.asset.AssetManagement.exception.ResourceNotFoundException;
import com.asset.AssetManagement.repository.AssetRepository;
import com.asset.AssetManagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.function.Consumer;

@Component
public class ValidatorUtil {

    private static EmployeeRepository employeeRepository;
    private static AssetRepository assetRepository;

    @Autowired
    public ValidatorUtil(EmployeeRepository empRepo, AssetRepository assetRepo) {
        ValidatorUtil.employeeRepository = empRepo;
        ValidatorUtil.assetRepository = assetRepo;
    }

    public static void validateAssetId(Long id) {
        if (!assetRepository.existsById(id)) {
            throw new ResourceNotFoundException(Constants.ASSET_NOT_FOUND + id);
        }
    }

    public static void validateEmployeeId(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException(Constants.EMPLOYEE_NOT_FOUND + id);
        }
    }

    public static void validateDuplicateSerial(String serialName, Long currentAssetId) {
        if (serialName != null) {
            boolean isDuplicate = assetRepository.existsBySerialName(serialName);

            if (isDuplicate) {
                Asset current = assetRepository.findById(currentAssetId).orElse(null);

                if (current != null && !serialName.equals(current.getSerialName())) {
                    throw new DuplicateAssetException(Constants.SERIAL_NUMBER_EXIST + serialName);
                }
            }
        }
    }

    public static void validateDate(LocalDate purchaseDate, LocalDate expireDate, LocalDate manufactureDate) {
        if (purchaseDate != null && expireDate != null) {
            if (expireDate.isBefore(purchaseDate)) {
                throw new InvalidAssetDateException(Constants.EXPIRE_BEFORE_PURCHASE);
            }
        }
        if (purchaseDate != null && purchaseDate.isAfter(LocalDate.now())) {
            throw new InvalidAssetDateException(Constants.PURCHASE_IN_FUTURE);
        }
        if (manufactureDate != null && manufactureDate.isAfter(LocalDate.now())) {
            throw new InvalidAssetDateException(Constants.MANUFACTURE_IN_FUTURE);
        }
    }

    public static void serialValid(String serialNumber) {
        if (assetRepository.existsBySerialName(serialNumber)) {
            throw new DuplicateAssetException(Constants.SERIAL_NUMBER_EXIST + serialNumber);
        }
    }

    public static boolean isValue(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public static <T> void updateIfPresent(T value, Consumer<T> set) {
        if (value != null) set.accept(value);
    }

    public static void updateIfValid(String value, Consumer<String> setter) {
        if (value != null && !value.trim().isEmpty()) {
            setter.accept(value);
        }
    }
}
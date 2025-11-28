package com.asset.AssetManagement.util;

import com.asset.AssetManagement.entity.Asset;
import com.asset.AssetManagement.exception.DuplicateAssetException;
import com.asset.AssetManagement.exception.InvalidAssetDateException;
import com.asset.AssetManagement.exception.ResourceNotFoundException;
import com.asset.AssetManagement.repository.AssetRepository;
import com.asset.AssetManagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidatorUtilTest {
    private AssetRepository assetRepository;
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setup() {
        assetRepository = mock(AssetRepository.class);
        employeeRepository = mock(EmployeeRepository.class);
        ValidatorUtil validator = new ValidatorUtil(employeeRepository, assetRepository);
    }

    @Test
    void testValidateAssetId_Exists() {
        when(assetRepository.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> ValidatorUtil.validateAssetId(1L));
    }

    @Test
    void testValidateAssetId_NotExists() {
        when(assetRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> ValidatorUtil.validateAssetId(1L));
    }

    @Test
    void testValidateEmployeeId_Exists() {
        when(employeeRepository.existsById(10L)).thenReturn(true);
        assertDoesNotThrow(() -> ValidatorUtil.validateEmployeeId(10L));
    }

    @Test
    void testValidateEmployeeId_NotExists() {
        when(employeeRepository.existsById(10L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> ValidatorUtil.validateEmployeeId(10L));
    }

    @Test
    void testValidateDuplicateSerial_NoDuplicate() {
        when(assetRepository.existsBySerialName("SN123")).thenReturn(false);
        assertDoesNotThrow(() -> ValidatorUtil.validateDuplicateSerial("SN123", 1L));
    }

    @Test
    void testValidateDuplicateSerial_DuplicateSameAsset() {
        Asset asset = new Asset();
        asset.setSerialName("SN123");
        when(assetRepository.existsBySerialName("SN123")).thenReturn(true);
        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        assertDoesNotThrow(() -> ValidatorUtil.validateDuplicateSerial("SN123", 1L));
    }

    @Test
    void testValidateDuplicateSerial_DuplicateOtherAsset() {
        Asset asset = new Asset();
        asset.setSerialName("OLD_SN");
        when(assetRepository.existsBySerialName("SN123")).thenReturn(true);
        when(assetRepository.findById(1L)).thenReturn(Optional.of(asset));
        assertThrows(DuplicateAssetException.class, () -> ValidatorUtil.validateDuplicateSerial("SN123", 1L));
    }

    @Test
    void testValidateDate_Valid() {
        assertDoesNotThrow(() ->
                ValidatorUtil.validateDate(LocalDate.now().minusDays(5),
                        LocalDate.now().plusDays(10),
                        LocalDate.now().minusDays(10)));
    }

    @Test
    void testValidateDate_ExpireBeforePurchase() {
        assertThrows(InvalidAssetDateException.class, () ->
                ValidatorUtil.validateDate(LocalDate.now(), LocalDate.now().minusDays(1), LocalDate.now().minusDays(5)));
    }

    @Test
    void testValidateDate_PurchaseFuture() {
        assertThrows(InvalidAssetDateException.class, () ->
                ValidatorUtil.validateDate(LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), LocalDate.now()));
    }

    @Test
    void testValidateDate_ManufactureFuture() {
        assertThrows(InvalidAssetDateException.class, () ->
                ValidatorUtil.validateDate(LocalDate.now().minusDays(1), LocalDate.now().plusDays(10), LocalDate.now().plusDays(1)));
    }

    @Test
    void testSerialValid_NoDuplicate() {
        when(assetRepository.existsBySerialName("SN999")).thenReturn(false);
        assertDoesNotThrow(() -> ValidatorUtil.serialValid("SN999"));
    }

    @Test
    void testSerialValid_Duplicate() {
        when(assetRepository.existsBySerialName("SN999")).thenReturn(true);
        assertThrows(DuplicateAssetException.class, () -> ValidatorUtil.serialValid("SN999"));
    }

    @Test
    void testIsValue() {
        assertTrue(ValidatorUtil.isValue("Hello"));
        assertFalse(ValidatorUtil.isValue(""));
        assertFalse(ValidatorUtil.isValue("  "));
        assertFalse(ValidatorUtil.isValue(null));
    }

    @Test
    void testUpdateIfPresent() {
        AtomicBoolean flag = new AtomicBoolean(false);
        ValidatorUtil.updateIfPresent("Value", v -> flag.set(true));
        assertTrue(flag.get());
    }

    @Test
    void testUpdateIfPresent_Null() {
        AtomicBoolean flag = new AtomicBoolean(false);
        ValidatorUtil.updateIfPresent(null, v -> flag.set(true));
        assertFalse(flag.get());
    }

    @Test
    void testUpdateIfValid() {
        AtomicBoolean flag = new AtomicBoolean(false);
        ValidatorUtil.updateIfValid("Valid", v -> flag.set(true));
        assertTrue(flag.get());
    }

    @Test
    void testUpdateIfValid_Invalid() {
        AtomicBoolean flag = new AtomicBoolean(false);
        ValidatorUtil.updateIfValid("  ", v -> flag.set(true));
        assertFalse(flag.get());
    }
}

package com.asset.AssetManagement.repository;
import com.asset.AssetManagement.entity.Asset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AssetRepositoryTest {

    @Autowired
    private AssetRepository assetRepository;

    @Test
    void testExistsBySerialName_WhenSerialExists() {
        Asset asset = new Asset();
        asset.setModelName("Laptop");
        asset.setSerialName("SN123");
        asset.setManufactureDate(null);
        asset.setPurchaseDate(null);
        asset.setExpireDate(null);
        assetRepository.save(asset);

        boolean exists = assetRepository.existsBySerialName("SN123");
        assertTrue(exists);
    }

    @Test
    void testExistsBySerialName_WhenSerialDoesNotExist() {
        boolean exists = assetRepository.existsBySerialName("UNKNOWN");
        assertFalse(exists);
    }
}

package com.asset.AssetManagement.service;
import com.asset.AssetManagement.entity.ArchivedAsset;
import com.asset.AssetManagement.entity.Asset;
import com.asset.AssetManagement.enums.AssetStatus;
import com.asset.AssetManagement.enums.AssetType;
import com.asset.AssetManagement.repository.ArchivedAssetRepository;
import com.asset.AssetManagement.repository.AssetRepository;
import com.asset.AssetManagement.service.Impl.ExpiredAssetServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;

import static com.asset.AssetManagement.enums.AssetStatus.ACTIVE;
import static com.asset.AssetManagement.enums.AssetType.LAPTOP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpiredAssetServiceImplTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private ArchivedAssetRepository archivedAssetRepository;

    @InjectMocks
    private ExpiredAssetServiceImpl expiredAssetService;

    @Test
    void testMoveExpiredAssets() {

        Asset asset = new Asset();
        asset.setModelName("Dell");
        asset.setSerialName("SN123");
        asset.setManufactureDate(LocalDate.of(2020, 1, 1));
        asset.setExpireDate(LocalDate.now().minusDays(1));
        asset.setPurchaseDate(LocalDate.of(2020, 2, 1));
        asset.setStatus(ACTIVE);
        asset.setType(LAPTOP);


        when(assetRepository.findByExpireDateBefore(any()))
                .thenReturn(List.of(asset));

        expiredAssetService.moveExpiredAssets();
        ArgumentCaptor<ArchivedAsset> archivedCaptor = ArgumentCaptor.forClass(ArchivedAsset.class);
        verify(archivedAssetRepository, times(1)).save(archivedCaptor.capture());

        ArchivedAsset savedArchived = archivedCaptor.getValue();
        assertThat(savedArchived.getModelName()).isEqualTo("Dell");
        assertThat(savedArchived.getSerialName()).isEqualTo("SN123");
        assertThat(savedArchived.getStatus()).isEqualTo(ACTIVE);
        assertThat(savedArchived.getType()).isEqualTo(LAPTOP);
        verify(assetRepository, times(1)).deleteAll(List.of(asset));
        verifyNoMoreInteractions(assetRepository, archivedAssetRepository);
    }
}


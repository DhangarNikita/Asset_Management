package com.asset.AssetManagement.config;

import com.asset.AssetManagement.dto.AssetCsvDto;
import com.asset.AssetManagement.entity.Asset;
import com.asset.AssetManagement.enums.AssetStatus;
import com.asset.AssetManagement.enums.AssetType;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.transaction.PlatformTransactionManager;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetBatchConfigTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private FlatFileItemReader<AssetCsvDto> reader;

    @Mock
    private ItemProcessor<AssetCsvDto, Asset> processor;

    @Mock
    private ItemWriter<Asset> writer;

    @Mock
    private Step assetStep;

    @Mock
    private JobExecutionListener listener;

    @Mock
    private EntityManagerFactory emf;

    @InjectMocks
    private AssetBatchConfig config;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAssetItemReader() {
        String filePath = "dummy.csv";
        FlatFileItemReader<AssetCsvDto> reader = config.assetItemReader(filePath);
        assertNotNull(reader);
        assertEquals(FlatFileItemReader.class, reader.getClass());
    }

    @Test
    void testAssetItemProcessor() throws Exception {
        AssetCsvDto dto = new AssetCsvDto(
                "Dell",
                "SN123",
                "10-01-2024",
                "10-01-2026",
                "10-01-2024",
                "ACTIVE",
                "LAPTOP"
        );
        ItemProcessor<AssetCsvDto, Asset> processor = config.assetItemProcessor();
        Asset asset = processor.process(dto);
        assertNotNull(asset);
        assertEquals("Dell", asset.getModelName());
        assertEquals(LocalDate.of(2024,1,10), asset.getManufactureDate());
        assertEquals(AssetStatus.ACTIVE, asset.getStatus());
        assertEquals(AssetType.LAPTOP, asset.getType());
    }

    @Test
    void testAssetItemWriter() {
        ItemWriter<Asset> writer = config.assetItemWriter(emf);
        assertNotNull(writer);
    }

    @Test
    void testAssetStep() {
        Step step = config.assetStep(jobRepository,transactionManager, reader, processor, writer);
        assertNotNull(step);
        assertEquals("assetStep", step.getName());
    }

    @Test
    void testAssetJob() {
        when(assetStep.getName()).thenReturn("assetStep");
        Job job = config.assetJob(jobRepository, assetStep, listener);
        assertNotNull(job);
        assertEquals("assetJob", job.getName());
    }
}

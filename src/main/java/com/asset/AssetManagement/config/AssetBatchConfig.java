package com.asset.AssetManagement.config;

import com.asset.AssetManagement.constants.Constants;
import com.asset.AssetManagement.dto.AssetCsvDto;
import com.asset.AssetManagement.entity.Asset;
import com.asset.AssetManagement.enums.AssetStatus;
import com.asset.AssetManagement.enums.AssetType;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class AssetBatchConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<AssetCsvDto> assetItemReader(@Value("#{jobParameters['filePath']}") String filePath) {
        FlatFileItemReader<AssetCsvDto> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(Constants.LINE_SKIP);

        DefaultLineMapper<AssetCsvDto> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(Constants.MODEL_NAME, Constants.SERIAL_NAME, Constants.MANUFACTURE_DATE, Constants.EXPIRE_DATE, Constants.PURCHASE_DATE, Constants.STATUS, Constants.TYPE);
        lineMapper.setLineTokenizer(tokenizer);

        lineMapper.setFieldSetMapper(fieldSet -> new AssetCsvDto(
            fieldSet.readString(Constants.MODEL_NAME),
            fieldSet.readString(Constants.SERIAL_NAME),
            fieldSet.readString(Constants.MANUFACTURE_DATE),
            fieldSet.readString(Constants.EXPIRE_DATE),
            fieldSet.readString(Constants.PURCHASE_DATE),
            fieldSet.readString(Constants.STATUS),
            fieldSet.readString(Constants.TYPE)
        ));
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public ItemProcessor<AssetCsvDto, Asset> assetItemProcessor() {
        return item -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
            return Asset.builder()
                    .modelName(item.getModelName())
                    .serialName(item.getSerialName())
                    .manufactureDate(LocalDate.parse(item.getManufactureDate(), formatter))
                    .expireDate(LocalDate.parse(item.getExpireDate(), formatter))
                    .purchaseDate(LocalDate.parse(item.getPurchaseDate(), formatter))
                    .status(AssetStatus.valueOf(item.getStatus().toUpperCase()))
                    .type(AssetType.valueOf(item.getType().toUpperCase()))
                    .build();
        };
    }


    @Bean
    public ItemWriter<Asset> assetItemWriter(EntityManagerFactory emf){
        return new JpaItemWriterBuilder<Asset>()
                .entityManagerFactory(emf)
                .build();
    }

    @Bean
    public Step assetStep(JobRepository jobRepository,
                          PlatformTransactionManager transactionManager,
                          FlatFileItemReader<AssetCsvDto> reader,
                          ItemProcessor<AssetCsvDto, Asset> processor,
                          ItemWriter<Asset> writer) {
        return new StepBuilder(Constants.ASSET_STEP, jobRepository)
                .<AssetCsvDto, Asset>chunk(Constants.CHUNK_SIZE, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job assetJob(JobRepository jobRepository, Step assetStep, JobExecutionListener listener) {
        return new JobBuilder(Constants.ASSET_JOB, jobRepository)
                .listener(listener)
                .start(assetStep)
                .build();
    }
}

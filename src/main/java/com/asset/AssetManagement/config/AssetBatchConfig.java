package com.asset.AssetManagement.config;

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
        reader.setLinesToSkip(1);

        DefaultLineMapper<AssetCsvDto> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames("modelName", "serialName", "manufactureDate", "expireDate", "purchaseDate", "status", "type");
        lineMapper.setLineTokenizer(tokenizer);

        lineMapper.setFieldSetMapper(fieldSet -> new AssetCsvDto(
            fieldSet.readString("modelName"),
            fieldSet.readString("serialName"),
            fieldSet.readString("manufactureDate"),
            fieldSet.readString("expireDate"),
            fieldSet.readString("purchaseDate"),
            fieldSet.readString("status"),
            fieldSet.readString("type")
        ));
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public ItemProcessor<AssetCsvDto, Asset> assetItemProcessor() {
        return item -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
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
        return new StepBuilder("assetStep", jobRepository)
                .<AssetCsvDto, Asset>chunk(1000, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job assetJob(JobRepository jobRepository, Step assetStep, JobExecutionListener listener) {
        return new JobBuilder("assetJob", jobRepository)
                .listener(listener)
                .start(assetStep)
                .build();
    }
}

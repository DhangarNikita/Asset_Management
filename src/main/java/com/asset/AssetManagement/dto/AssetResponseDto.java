package com.asset.AssetManagement.dto;

import com.asset.AssetManagement.enums.AssetStatus;
import com.asset.AssetManagement.enums.AssetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetResponseDto {

    private Long assetId;
    private String modelName;
    private String serialName;
    private LocalDate manufactureDate;
    private LocalDate expireDate;
    private LocalDate purchaseDate;
    private String assignTo;
    private AssetStatus status;
    private AssetType type;
    private Long employeeId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

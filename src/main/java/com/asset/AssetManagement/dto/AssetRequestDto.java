package com.asset.AssetManagement.dto;

import com.asset.AssetManagement.enums.AssetStatus;
import com.asset.AssetManagement.enums.AssetType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AssetRequestDto {

    @NotBlank(message = "Model name is required")
    private String modelName;

    @NotBlank(message = "Serial name is required")
    private String serialName;

    @PastOrPresent(message = "Manufacture date cannot be in the future")
    private LocalDate manufactureDate;

    @FutureOrPresent(message = "Expire date must be today or later")
    private LocalDate expireDate;

    @PastOrPresent(message = "Purchase date cannot be in the future")
    private LocalDate purchaseDate;

    @NotNull(message = "Cost is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Cost must be greater than 0")
    private BigDecimal cost;

    private AssetStatus status = AssetStatus.ACTIVE;

    private AssetType type = AssetType.OTHER;

   // @NotNull(message = "Employee ID is required")
    private Long employeeId;
}

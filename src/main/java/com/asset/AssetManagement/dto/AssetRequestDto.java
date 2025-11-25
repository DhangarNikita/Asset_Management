package com.asset.AssetManagement.dto;

import com.asset.AssetManagement.enums.AssetStatus;
import com.asset.AssetManagement.enums.AssetType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    private String assignTo;

    private AssetStatus status = AssetStatus.ACTIVE;

    private AssetType type = AssetType.OTHER;

    // @NotNull(message = "Employee ID is required")
    private Long employeeId;
}

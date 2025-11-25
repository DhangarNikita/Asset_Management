package com.asset.AssetManagement.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignAssetRequestDto {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;
}


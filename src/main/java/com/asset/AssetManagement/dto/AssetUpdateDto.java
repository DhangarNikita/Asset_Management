package com.asset.AssetManagement.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import java.time.LocalDate;
import com.asset.AssetManagement.enums.AssetStatus;
import com.asset.AssetManagement.enums.AssetType;

@Data
public class AssetUpdateDto {

    private String modelName;
    private String serialName;
    @PastOrPresent(message = "Manufacture date cannot be in the future")
    private LocalDate manufactureDate;
    @PastOrPresent(message = "Purchase date cannot be in the future")
    private LocalDate purchaseDate;
    @FutureOrPresent(message = "Expire date must be today or later")
    private LocalDate expireDate;
    private AssetStatus status;
    private AssetType type;
}


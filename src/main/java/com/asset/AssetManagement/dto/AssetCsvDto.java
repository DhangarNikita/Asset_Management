package com.asset.AssetManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetCsvDto {
    private String modelName;
    private String serialName;
    private String manufactureDate;
    private String expireDate;
    private String purchaseDate;
    private String status;
    private String type;
}


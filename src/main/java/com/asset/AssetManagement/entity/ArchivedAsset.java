package com.asset.AssetManagement.entity;

import com.asset.AssetManagement.enums.AssetStatus;
import com.asset.AssetManagement.enums.AssetType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class ArchivedAsset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String modelName;
    private String serialName;
    private LocalDate manufactureDate;
    private LocalDate expireDate;
    private LocalDate purchaseDate;
    @Enumerated(EnumType.STRING)
    private AssetStatus status;
    @Enumerated(EnumType.STRING)
    private AssetType type;
}

package com.asset.AssetManagement.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(nullable = false, length = 255)
    private String employeeName;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(length = 255)
    private String department;

    @Column(length = 20)
    private String phoneNumber;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<Asset> assets;
}


package com.asset.AssetManagement.dao;

import com.asset.AssetManagement.entity.Employee;
import com.asset.AssetManagement.exception.ResourceNotFoundException;
import com.asset.AssetManagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDao {
    @Autowired
    private EmployeeRepository employeeRepository;
    private static final Long DEFAULT_EMPLOYEE_ID = 0L;
    public Employee getEmployeeOrDefault(Long employeeId) {
        Long id = (employeeId == null) ? DEFAULT_EMPLOYEE_ID : employeeId;
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for id: " + id));
    }
}


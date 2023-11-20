package com.project.DuAnTotNghiep.service;

import com.project.DuAnTotNghiep.dto.CustomerDto.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CustomerService {
    Page<CustomerDto> getAllCustomers(Pageable pageable);

    CustomerDto createCustomer(CustomerDto customerDto);

    Page<CustomerDto> searchCustomer(String keyword, Pageable pageable);
}

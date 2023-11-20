package com.project.DuAnTotNghiep.controller;

import com.project.DuAnTotNghiep.dto.CustomerDto.CustomerDto;
import com.project.DuAnTotNghiep.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
public class CustomerRestController {

    private final CustomerService customerService;

    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping("/api/customer")
    public Page<CustomerDto> getAllCustomers(Pageable pageable) {
        return customerService.getAllCustomers(pageable);
    }

    @GetMapping("/api/customer/filter")
    public Page<CustomerDto> searchCustomers(@RequestParam String keyword,  Pageable pageable) {
        return customerService.searchCustomer(keyword, pageable);
    }

    @PostMapping("/customer/create")
    public CustomerDto createCustomerFormData(@ModelAttribute CustomerDto customerDto) {
        return customerService.createCustomer(customerDto);
    }

    @PostMapping("/api/customer")
    public CustomerDto createCustomer(@RequestBody CustomerDto customerDto) {
        return customerService.createCustomer(customerDto);
    }


}

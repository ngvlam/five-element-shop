package com.project.DuAnTotNghiep.repository;

import com.project.DuAnTotNghiep.dto.CustomerDto.CustomerDto;
import com.project.DuAnTotNghiep.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByCode(String code);

    Customer findTopByOrderByIdDesc();

    @Query("SELECT c FROM Customer c WHERE c.code LIKE %:keyword% OR c.name LIKE %:keyword% OR c.phoneNumber LIKE %:keyword%")
    Page<Customer> searchCustomerKeyword(String keyword,Pageable pageable);
}

package com.project.DuAnTotNghiep.repository;

import com.project.DuAnTotNghiep.entity.ProductDiscount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDiscountRepository extends JpaRepository<ProductDiscount, Long> {
    Page<ProductDiscount> findAllByProductDetailNotNull(Pageable pageable);
}

package com.project.DuAnTotNghiep.repository;

import com.project.DuAnTotNghiep.entity.Color;
import com.project.DuAnTotNghiep.entity.Product;
import com.project.DuAnTotNghiep.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SizeRepository extends JpaRepository<Size, Long> {
    @Query(value = "select distinct s from Size s join ProductDetail pd on s.id = pd.size.id where pd.product = :product")
    List<Size> findSizesByProduct(Product product);

    @Query(value = "select distinct s from Size s join ProductDetail pd on s.id = pd.size.id where pd.product = :product and pd.color = :color")
    List<Size> findSizesByProductAndColor(Product product, Color color);
}
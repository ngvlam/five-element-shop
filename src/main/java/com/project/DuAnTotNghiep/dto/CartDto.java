package com.project.DuAnTotNghiep.dto;

import com.project.DuAnTotNghiep.dto.Product.ProductDetailDto;
import com.project.DuAnTotNghiep.dto.Product.ProductDto;
import com.project.DuAnTotNghiep.entity.Color;
import com.project.DuAnTotNghiep.entity.Image;
import com.project.DuAnTotNghiep.entity.ProductDetail;
import com.project.DuAnTotNghiep.entity.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long id;
    private Long accountId;
    private ProductCart product;
    private ProductDetailDto detail;

    @NotNull
    private int quantity;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;

}


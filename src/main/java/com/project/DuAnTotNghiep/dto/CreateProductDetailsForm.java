package com.project.DuAnTotNghiep.dto;

import com.project.DuAnTotNghiep.entity.ProductDetail;
import lombok.Data;

import java.util.List;

@Data
public class CreateProductDetailsForm {
    private List<ProductDetail> productDetailList;
}

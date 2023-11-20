package com.project.DuAnTotNghiep.dto;

import com.project.DuAnTotNghiep.entity.Brand;
import com.project.DuAnTotNghiep.entity.Category;
import com.project.DuAnTotNghiep.entity.Image;
import com.project.DuAnTotNghiep.entity.Product;
import com.project.DuAnTotNghiep.entity.ProductDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataForm {
    private ProductDetail productDetail;
    private Image image;

    public Image getImage() {
        return image;
    }

    public ProductDetail getProductDetail() {
        return productDetail;
    }

}

package com.project.DuAnTotNghiep.controller.admin;

import com.project.DuAnTotNghiep.entity.Category;
import com.project.DuAnTotNghiep.entity.ProductDiscount;
import com.project.DuAnTotNghiep.repository.ProductDetailRepository;
import com.project.DuAnTotNghiep.repository.ProductDiscountRepository;
import com.project.DuAnTotNghiep.repository.ProductRepository;
import com.project.DuAnTotNghiep.service.CategoryService;
import com.project.DuAnTotNghiep.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductDiscountController {
    private final ProductService productService;

    private final CategoryService categoryService;
    private final ProductDetailRepository productDetailRepository;
    private final ProductDiscountRepository productDiscountRepository;

    public ProductDiscountController(ProductService productService, ProductRepository productRepository, CategoryService categoryService, ProductDetailRepository productDetailRepository, ProductDiscountRepository productDiscountRepository) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.productDetailRepository = productDetailRepository;
        this.productDiscountRepository = productDiscountRepository;
    }

    @GetMapping("/admin/product-discount")
    public String viewProductDiscountPage(Model model, Pageable pageable) {
        List<ProductDiscount> productDiscountList = productDiscountRepository.findAll();
        model.addAttribute("productDiscounts", productDiscountList);
        return "/admin/product-discount";
    }

    @GetMapping("/admin/product-discount-create")
    public String viewProductDiscountCreatePage(Model model) {
        List<Category> categories = categoryService.getAll();
        model.addAttribute("categories", categories);
        return "/admin/product-discount-create";
    }
}

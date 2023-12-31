package com.project.DuAnTotNghiep.controller.admin;


import com.project.DuAnTotNghiep.dto.DataForm;
import com.project.DuAnTotNghiep.dto.Product.ProductDto;
import com.project.DuAnTotNghiep.entity.Color;
import com.project.DuAnTotNghiep.entity.Image;
import com.project.DuAnTotNghiep.entity.Product;
import com.project.DuAnTotNghiep.entity.ProductDetail;
import com.project.DuAnTotNghiep.entity.Size;
import com.project.DuAnTotNghiep.service.ColorService;
import com.project.DuAnTotNghiep.service.ImageService;
import com.project.DuAnTotNghiep.service.ProductDetailService;
import com.project.DuAnTotNghiep.service.ProductService;
import com.project.DuAnTotNghiep.service.SizeService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ProductDetailController {

    private Product productInLine;
    private final List<Image> imageList = new ArrayList<>();
    private long idImage;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SizeService sizeService;
    @Autowired
    private ColorService colorService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/admin/chi-tiet-san-pham/{code}")
    public String getProductDetailPage(@PathVariable String code, Model model) {
        Product product = productService.getProductByCode(code);
        if(product != null) {
            model.addAttribute("product", product);
            model.addAttribute("productDetails", product.getProductDetails());
            return "admin/product-detail";
        }

        return "error/404";
    }

    @ModelAttribute("listSize")
    public List<Size> getSize() {
        return sizeService.getAll();
    }

    @ModelAttribute("listColor")
    public List<Color> getColor() {
        return colorService.findAll();
    }
}

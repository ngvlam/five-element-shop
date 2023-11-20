package com.project.DuAnTotNghiep.controller.user;


import com.project.DuAnTotNghiep.entity.Product;
import com.project.DuAnTotNghiep.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String gethome(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                          @RequestParam(name = "sort", defaultValue = "name,asc") String sortField) {
        int pageSize = 20;
        String[] sortParams = sortField.split(",");
        String sortFieldName = sortParams[0];
        Sort.Direction sortDirection = Sort.Direction.ASC;

        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) {
            sortDirection = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(sortDirection, sortFieldName);

        Pageable pageable = PageRequest.of(page, pageSize, sort);

        Page<Product> products = productService.getAllByStatus(1, pageable);
        model.addAttribute("products", products);

        return "user/home-03";
    }


//    @GetMapping("getDetail/{code}")
//    public String getDetail(Model model, @PathVariable("code") String code) {
//        model.addAttribute("detail", productService.findById(code).get(0));
//        List<Product> products = productService.findById(code);
//        List<Size> images = new ArrayList<>();
//        for (Product product : products){
//            images.add(product.getProductDetails().get())
//        }
//        return "user/product-detail";
//    }

}

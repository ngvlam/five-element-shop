package com.project.DuAnTotNghiep.controller.admin;

import com.project.DuAnTotNghiep.entity.Brand;
import com.project.DuAnTotNghiep.entity.Color;
import com.project.DuAnTotNghiep.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/brand-all")
    public String getAllBrand(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "sort", defaultValue = "name,asc") String sortField) {
        int pageSize = 5; // Number of items per page
        String[] sortParams = sortField.split(",");
        String sortFieldName = sortParams[0];
        Sort.Direction sortDirection = Sort.Direction.ASC;

        if (sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")) {
            sortDirection = Sort.Direction.DESC;
        }

        Sort sort = Sort.by(sortDirection, sortFieldName);

        Pageable pageable = PageRequest.of(page, pageSize, sort);
        Page<Brand> brandPage = brandService.getAllBrand(pageable);

        model.addAttribute("sortField", sortFieldName);
        model.addAttribute("sortDirection", sortDirection);

        model.addAttribute("items", brandPage);

        return "admin/brand";
    }

    @GetMapping("/brand-create")
    public String viewAddBrand(Model model){
        Brand brand = new Brand();
        model.addAttribute("action", "/admin/brand-save");
        model.addAttribute("Brand", brand);
        return "admin/brand-create";
    }


    @PostMapping("/brand-save")
    public String addBrand(Model model, @Validated @ModelAttribute("Brand") Brand brand) {
        brandService.save(brand);
        return "redirect:/admin/brand-all";
    }

    @PostMapping("/brand-update/{id}")
    public String update(@PathVariable("id") Long id,
                         @Validated @ModelAttribute("Brand") Brand brand) {
        Optional<Brand> optional = brandService.findById(id);
        if (optional.isPresent()) {
            Brand updateBrand = optional.get();
            updateBrand.setName(brand.getName());
            updateBrand.setCode(brand.getCode());
            brandService.save(updateBrand);
            return "redirect:/admin/brand-all";
        } else {
            return null;
        }
    }

    @GetMapping("/brand-detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Optional<Brand> optional = brandService.findById(id);
        if (optional.isPresent()) {
            Brand brand = optional.get();
            model.addAttribute("Brand", brand);
            model.addAttribute("action", "/admin/brand-update/" + brand.getId());
            return "admin/brand-create";
        } else {
            return null;
        }
    }

    @GetMapping("/brand-delete/{id}")
    public String delete(@PathVariable("id") Long id, ModelMap modelMap){
        brandService.delete(id);
        return "redirect:/admin/brand-all";
    }
}

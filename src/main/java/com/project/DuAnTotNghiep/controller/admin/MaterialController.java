package com.project.DuAnTotNghiep.controller.admin;

import com.project.DuAnTotNghiep.entity.Brand;
import com.project.DuAnTotNghiep.entity.Material;
import com.project.DuAnTotNghiep.service.BrandService;
import com.project.DuAnTotNghiep.service.MaterialService;
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
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping("/material-all")
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
        Page<Material> materialPage = materialService.getAllMaterial(pageable);

        model.addAttribute("sortField", sortFieldName);
        model.addAttribute("sortDirection", sortDirection);

        model.addAttribute("items", materialPage);

        return "admin/material";
    }

    @GetMapping("/material-create")
    public String viewAddBrand(Model model){
        Material material = new Material();
        model.addAttribute("action", "/admin/material-save");
        model.addAttribute("Material", material);
        return "admin/material-create";
    }


    @PostMapping("/material-save")
    public String addBrand(Model model, @Validated @ModelAttribute("Material") Material material) {
        materialService.save(material);
        return "redirect:/admin/material-all";
    }

    @PostMapping("/material-update/{id}")
    public String update(@PathVariable("id") Long id,
                         @Validated @ModelAttribute("Brand") Brand brand) {
        Optional<Material> optional = materialService.findById(id);
        if (optional.isPresent()) {
            Material updateMaterial = optional.get();
            updateMaterial.setName(brand.getName());
            updateMaterial.setCode(brand.getCode());
            materialService.save(updateMaterial);
            return "redirect:/admin/material-all";
        } else {
            return null;
        }
    }

    @GetMapping("/material-detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Optional<Material> optional = materialService.findById(id);
        if (optional.isPresent()) {
            Material material = optional.get();
            model.addAttribute("Material", material);
            model.addAttribute("action", "/admin/material-update/" + material.getId());
            return "admin/material-create";
        } else {
            return null;
        }
    }

    @GetMapping("/material-delete/{id}")
    public String delete(@PathVariable("id") Long id, ModelMap modelMap){
        materialService.delete(id);
        return "redirect:/admin/material-all";
    }
}

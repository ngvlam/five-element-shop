package com.project.DuAnTotNghiep.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminHomeController {
    @GetMapping("/admin")
    public String viewAdminHome() {
        return "/admin/index";
    }
}

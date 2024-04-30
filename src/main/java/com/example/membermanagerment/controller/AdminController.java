package com.example.membermanagerment.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
    @GetMapping("/admin-thanhvien")
    public String admin_thanhvien() {
        return "admin-thanhvien";
    }
    @GetMapping("/admin-thietbi")
    public String admin_thietbi() {
        return "admin-thietbi";
    }
    @GetMapping("/admin-vipham")
    public String admin_vipham() {
        return "admin-vipham";
    }
}

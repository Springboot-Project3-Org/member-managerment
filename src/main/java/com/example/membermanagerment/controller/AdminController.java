package com.example.membermanagerment.controller;
import com.example.membermanagerment.model.ThanhVien;
import com.example.membermanagerment.repository.ThanhVienRepository;
import com.example.membermanagerment.validate.validate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.util.Validate;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

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

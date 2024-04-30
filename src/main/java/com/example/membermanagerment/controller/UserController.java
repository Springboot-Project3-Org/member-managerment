package com.example.membermanagerment.controller;

import com.example.membermanagerment.model.ThanhVien;
import com.example.membermanagerment.repository.ThanhVienRepository;
import com.example.membermanagerment.validate.validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private ThanhVienRepository thanhVienRepository;

    @Autowired
    private JavaMailSender emailSender;
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/loginHandle")
    @ResponseBody
    public Map<String, Object> processLogin(@RequestBody Map<String, String> loginData) {
        String idOrEmail = loginData.get("idOrEmail");
        String password = loginData.get("password");

        Map<String, Object> response = new HashMap<>();

        for(ThanhVien member : thanhVienRepository.findAll()) {
            String validMaSV = String.valueOf(member.getMaTV());
            if(member.getEmail().equals(idOrEmail) && member.getPassword().equals(password)
                || validMaSV.equals(idOrEmail) && member.getPassword().equals(password)) {
                response.put("success", true);
                response.put("memberID", member.getMaTV());
                response.put("message", "Xin chào: "+idOrEmail);
                return response;
            }
        }
        response.put("success", false);
        response.put("message", "Đăng nhập thất bại");
        return response;
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/registerHandle")
    @ResponseBody
    public Map<String, Object> registerHandle(@RequestBody Map<String, String> loginData) {
        Map<String, Object> response = new HashMap<>();
        String memberIDString = loginData.get("memberID");
        BigInteger memberID = null;
        try {
            memberID = new BigInteger(memberIDString);
        } catch (NumberFormatException e) {
            response.put("success", false);
            response.put("message", "Member ID is not valid");
            return response;
        }
        String fullName = loginData.get("fullName");
        String faculty = loginData.get("faculty");
        String major = loginData.get("major");
        String phone = loginData.get("phone");
        String email = loginData.get("email");
        String password = loginData.get("password");

        boolean isMemberIDValid = validate.validateStudentID(memberIDString);
        boolean isPhoneNumberValid = validate.validatePhoneNumber(phone);
        boolean isEmailValid = validate.validateEmail(email);

        if (!isMemberIDValid) {
            response.put("success", false);
            response.put("message", "Member ID is not valid");
            return response;
        } else if (!isPhoneNumberValid) {
            response.put("success", false);
            response.put("message", "Phone number is not valid");
            return response;
        } else if (!isEmailValid) {
            response.put("success", false);
            response.put("message", "Email is not valid");
            return response;
        } else {
            for(ThanhVien member : thanhVienRepository.findAll()) {
                if(memberID.compareTo(member.getMaTV()) == 0) {
                    response.put("success", false);
                    response.put("message", "Existed MaTV");
                    return response;
                }else if(email.equals(member.getEmail())) {
                    response.put("success", false);
                    response.put("message", "Existed email");
                    return response;
                }else if(phone.equals(member.getSdt())) {
                    response.put("success", false);
                    response.put("message", "Existed phone number");
                    return response;
                }
            }
            ThanhVien newMember = new ThanhVien(memberID, fullName, faculty, major, phone, password, email);
            ThanhVien addedMember = thanhVienRepository.save(newMember);
            response.put("success", addedMember != null);
            if (addedMember != null) {
                response.put("message", "Registration successful");
            } else {
                response.put("message", "Registration failed");
            }
        }

        return response;
    }


    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "forgot-password";
    }

    @PostMapping("/forgotHandle")
    @ResponseBody
    public Map<String, Object> forgotHandle(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        Map<String, Object> response = new HashMap<>();

        for(ThanhVien member : thanhVienRepository.findAll()) {
            if(member.getEmail().equals(email)) {
                String newPassword = generateRandomPassword();

                sendNewPasswordByEmail(email, newPassword);

                member.setPassword(newPassword);
                thanhVienRepository.save(member);

                response.put("email", email);
                response.put("success", true);
                response.put("message", "New password sent");
                return response;
            }
        }
        response.put("success", false);
        response.put("message", "Email doesn't exist");
        return response;
    }

    private String generateRandomPassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder newPassword = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 20; i++) {
            newPassword.append(characters.charAt(random.nextInt(characters.length())));
        }
        return newPassword.toString();
    }

    private void sendNewPasswordByEmail(String email, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your New Password");
        message.setText("Your new password is: " + newPassword);

        System.out.println(message);
        try {
            // Gửi email
            emailSender.send(message);
            System.out.println("Email sent successfully");
        } catch (MailException e) {
            e.printStackTrace();
            System.out.println("Failed to send email: " + e.getMessage());
        }
        System.out.println("OKK");
    }

    @GetMapping("/change-password")
    public String changePassword() {
        return "change-password";
    }

    @GetMapping("/home/{memberID}")
    public String home(@PathVariable BigInteger memberID, Model model) {
        ThanhVien member = thanhVienRepository.findById(memberID).orElse(null);
        model.addAttribute("member", member);
        return "user-homepage";
    }

    @PostMapping("/changeHandle")
    @ResponseBody
    public Map<String, Object> changePasswordHandle(@RequestBody Map<String, String> changeData) {
        Map<String, Object> response = new HashMap<>();

        String email = changeData.get("email");
        String oldPassword = changeData.get("oldPassword");
        String newPassword = changeData.get("password");

        for(ThanhVien member : thanhVienRepository.findAll()) {
            if(member.getEmail().equals(email)) {
                String currentPassword = member.getPassword();

                if(!currentPassword.equals(oldPassword)) {
                    response.put("success", false);
                    response.put("message", "Mật khẩu không khớp với mật khẩu trong email đã gửi(mật khẩu cũ)");
                    return response;
                }else {
                    System.out.println("newpassword: "+newPassword);
                    member.setPassword(newPassword);

                    System.out.println(member.toString());
                    thanhVienRepository.save(member);

                    response.put("success", true);
                    response.put("message", "Đổi mật khẩu thành công");
                    return response;
                }

            }
        }
        response.put("success", false);
        response.put("message", "Đổi mật khẩu không thành công");
        return response;
    }
}

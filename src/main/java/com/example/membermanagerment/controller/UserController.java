package com.example.membermanagerment.controller;

import com.example.membermanagerment.model.ThanhVien;
import com.example.membermanagerment.repository.ThanhVienRepository;
import com.example.membermanagerment.validate.validate;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        Object memberObj = session.getAttribute("memberID");
        if (memberObj == null) {
            return "redirect:/login";
        }

        BigInteger memberID;
        try {
            memberID = new BigInteger(memberObj.toString());
        } catch (NumberFormatException ex) {
            return "redirect:/login";
        }

        ThanhVien member = thanhVienRepository.findById(memberID).orElse(null);
        if (member == null) {
            return "redirect:/login";
        }
        model.addAttribute("member", member);
        return "user-homepage";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Xóa session
        session.invalidate();

        return "redirect:/login";
    }


    @GetMapping("/login")
    public String login(HttpSession session) {
        Object memberObj = session.getAttribute("memberID");
        return (memberObj != null) ? "redirect:/home" : "login";
    }

    @PostMapping("/loginHandle")
    @ResponseBody
    public Map<String, Object> processLogin(@RequestBody Map<String, String> loginData, HttpSession session) {
        String idOrEmail = loginData.get("idOrEmail");
        String password = loginData.get("password");

        Map<String, Object> response = new HashMap<>();
        ThanhVien member;

        try {
//            Nếu mà là mã thành viên thì sẽ không có lỗi
            BigInteger memberID = new BigInteger(idOrEmail);
            member = thanhVienRepository.findByMaTVAndPassword(memberID, password);
        } catch (NumberFormatException ex) {
            member = thanhVienRepository.findByEmailAndPassword(idOrEmail, password);
        }

        if(member != null) {
            response.put("success", true);
            response.put("memberID", member.getMaTV());
            response.put("message", "Xin chào: "+idOrEmail);
//            Them memberID vao session
            session.setAttribute("memberID", member.getMaTV());
            return response;
        }

        response.put("success", false);
        response.put("message", "Đăng nhập thất bại");
        return response;
    }

    @GetMapping("/register")
    public String register(HttpSession session) {
        Object memberObj = session.getAttribute("memberID");
        return (memberObj == null) ? "register" : "redirect:/home";
    }

    @PostMapping("/registerHandle")
    @ResponseBody
    public Map<String, Object> registerHandle(@RequestBody Map<String, String> loginData, HttpSession session) {
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
            if (thanhVienRepository.findById(memberID).isPresent()) {
                response.put("success", false);
                response.put("message", "Existed MaTV");
                return response;
            }
            if (thanhVienRepository.findByEmail(email) != null) {
                response.put("success", false);
                response.put("message", "Existed email");
                return response;
            }
            if (thanhVienRepository.findBySdt(phone) != null) {
                response.put("success", false);
                response.put("message", "Existed phone number");
                return response;
            }

            ThanhVien newMember = new ThanhVien(memberID, fullName, faculty, major, phone, password, email);
            ThanhVien addedMember = thanhVienRepository.save(newMember);
            response.put("success", addedMember != null);
            if (addedMember != null) {
                response.put("message", "Registration successful");
//                Them memberID vao session
                session.setAttribute("memberID", memberID);
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

        ThanhVien member = thanhVienRepository.findByEmail(email);

        if (member != null) {
            String newPassword = generateRandomPassword();

            sendNewPasswordByEmail(email, newPassword);

            member.setPassword(newPassword);
            thanhVienRepository.save(member);

            response.put("memberID", member.getMaTV());
            response.put("success", true);
            response.put("message", "New password sent");
            return response;
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

    @GetMapping("/change-password/{memberID}")
    public String changePassword(@PathVariable BigInteger memberID, Model model) {
        model.addAttribute("memberID", memberID);
        return "change-password";
    }

    @PostMapping("/changeHandle/{memberID}")
    @ResponseBody
    public Map<String, Object> changePasswordHandle(@PathVariable BigInteger memberID,
                                                    @RequestBody Map<String, String> changeData) {
        Map<String, Object> response = new HashMap<>();

        String oldPassword = changeData.get("oldPassword");
        String newPassword = changeData.get("password");

        ThanhVien member = thanhVienRepository.findById(memberID).orElse(null);

        if (member == null) {
            response.put("success", false);
            response.put("message", "Thành viên không tồn tại");
            return response;
        }

        String currentPassword = member.getPassword();

        if (!currentPassword.equals(oldPassword)) {
            response.put("success", false);
            response.put("message", "Mật khẩu không khớp với mật khẩu cũ");
        } else {
            member.setPassword(newPassword);

            thanhVienRepository.save(member);

            response.put("success", true);
            response.put("message", "Đổi mật khẩu thành công");
        }
        return response;
    }

    @GetMapping("/violation-status")
    public String violationStatus(Model model, HttpSession session) {
//        Lấy maTV từ session
        Object memberObj = session.getAttribute("memberID");
//        Nếu không có maTV từ session chuyển hướng sang đăng nhập
        if (memberObj == null) {
            return "redirect:/login";
        }

        BigInteger memberID;
        try {
            memberID = new BigInteger(memberObj.toString());
        } catch (NumberFormatException ex) {
            return "redirect:/login";
        }

        ThanhVien member = thanhVienRepository.findById(memberID).orElse(null);
        if (member == null) {
            return "redirect:/login";
        }
        model.addAttribute("member", member);

        return "user-violation-status";
    }

    @GetMapping("/borrowing-status")
    public String borrowingStatus(Model model, HttpSession session) {
//        Lấy maTV từ session
        Object memberObj = session.getAttribute("memberID");
//        Nếu không có maTV từ session chuyển hướng sang đăng nhập
        if (memberObj == null) {
            return "redirect:/login";
        }

        BigInteger memberID;
        try {
            memberID = new BigInteger(memberObj.toString());
        } catch (NumberFormatException ex) {
            return "redirect:/login";
        }

        ThanhVien member = thanhVienRepository.findById(memberID).orElse(null);
        if (member == null) {
            return "redirect:/login";
        }
        model.addAttribute("member", member);

        return "user-borrowing-status";
    }

    @GetMapping("/booking-status")
    public String bookingStatus(Model model, HttpSession session) {
//        Lấy maTV từ session
        Object memberObj = session.getAttribute("memberID");
//        Nếu không có maTV từ session chuyển hướng sang đăng nhập
        if (memberObj == null) {
            return "redirect:/login";
        }

        BigInteger memberID;
        try {
            memberID = new BigInteger(memberObj.toString());
        } catch (NumberFormatException ex) {
            return "redirect:/login";
        }

        ThanhVien member = thanhVienRepository.findById(memberID).orElse(null);
        if (member == null) {
            return "redirect:/login";
        }
        model.addAttribute("member", member);

        return "user-booking-status";
    }
}

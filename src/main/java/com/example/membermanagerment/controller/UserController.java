package com.example.membermanagerment.controller;

import com.example.membermanagerment.model.ThanhVien;
import com.example.membermanagerment.model.ThietBi;
import com.example.membermanagerment.model.ThongTinSD;
import com.example.membermanagerment.model.XuLy;
import com.example.membermanagerment.repository.ThanhVienRepository;
import com.example.membermanagerment.repository.ThietBiRepository;
import com.example.membermanagerment.repository.ThongTinSDRepository;
import com.example.membermanagerment.repository.XuLyRepository;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private ThanhVienRepository thanhVienRepository;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired 
    private ThietBiRepository thietBiRepository;
    @Autowired
    private ThongTinSDRepository thongTinSDRepository;
    @Autowired
    private XuLyRepository xuLyRepository;

    private ThietBi thietbi;
    private ThongTinSD thongTinSD;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    /**
     * @param model
     * @param session
     * @return
     */
    @GetMapping("/home")
    public String home(@RequestParam(name = "search", required = false)String search, Model model, HttpSession session) {
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
        String mssv = memberID.toString();
        List<ThietBi> thietbiList;
        if (search == null || search.isEmpty()) {
            thietbiList = thietBiRepository.findAll();
        } else {
            thietbiList = thietBiRepository.findByKeyword2(search);
        }
        model.addAttribute("thietbiList", thietbiList);
        return "user-homepage";
    }
    @RequestMapping(value = {"/home/save"}, method = RequestMethod.POST)
    public String save(Model model,  HttpSession session , @ModelAttribute("home") ThongTinSD thongTinSD) {
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
        String mssv = memberID.toString();
        BigInteger msSV = new BigInteger(mssv);
        int maTB = thietbi.getMaTB();
        Timestamp tgdatcho = thongTinSD.getTGDatCho();
        ThongTinSD v = new ThongTinSD(msSV, maTB, null, null, null, tgdatcho);
        ThongTinSD add = thongTinSDRepository.save(v);
        return "user-homepage";
    }
    @PostMapping("/addThongTinSD")
    @ResponseBody
    public Map<String, Object> addThongTinSD(@RequestBody Map<String, String> thongtinSD,HttpSession session,Model model) {
        Map<String, Object> response = new HashMap<>();
        Object memberObj = session.getAttribute("memberID");
        BigInteger memberID;
        memberID = new BigInteger(memberObj.toString());   
        String mssv = memberID.toString();
        BigInteger maTV = new BigInteger(mssv);
        List<XuLy> Xulylist = xuLyRepository.findByMaTVAndTrangThaiXL(maTV);
        if (Xulylist != null && !Xulylist.isEmpty()) {
            // Danh sách không rỗng, không được đặt chỗ
            response.put("success", false);
            response.put("message", "Bạn không được đặt chỗ vì vi phạm chưa được xử lý");
        } else {

            // Danh sách rỗng, thực hiện đặt chỗ mới
            int maTB = Integer.parseInt(thongtinSD.get("maTB"));
            Timestamp tgdatcho = Timestamp.valueOf(thongtinSD.get("tgdatcho"));
            List<ThongTinSD> existingDevices = thongTinSDRepository.findByMaTBAndTGDatCho(maTB, tgdatcho);
            if (!existingDevices.isEmpty()) {
                // Nếu đã tồn tại, ghi thông báo thiết bị đã được đặt chỗ
                response.put("success", false);
                response.put("message", "Thiết bị đã được đặt chỗ");
            } else {
                ThongTinSD newSD = new ThongTinSD(maTV, maTB, null, null, null, tgdatcho);
                ThongTinSD addedDevice = thongTinSDRepository.save(newSD);
                response.put("success", addedDevice != null);
                if (addedDevice != null) {
                    response.put("message", "Đặt chỗ thành công");
                } else {
                    response.put("message", "Đặt chỗ thất bại");
                }
            }
        }
        return response;
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
        String mssv = memberID.toString();
        List<XuLy> errorList = xuLyRepository.findByKeyword(mssv);
        model.addAttribute("errorList",errorList);

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

        // Lấy danh sách thông tin sử dụng của user hiện tại
        List<ThongTinSD> thongTinSDList = thongTinSDRepository.findByThanhvien(memberID);

        // Tạo danh sách để lưu thông tin cần thiết
        List<Map<String, Object>> thietbiList = new ArrayList<>();

        for (ThongTinSD thongTinSD : thongTinSDList) {
//            Nếu thông tin sử dụng có chứa thiết bị
            if (thongTinSD.getThietbi() != null) {
                // Tìm thiết bị tương ứng
                ThietBi thietBi = thietBiRepository.findById(thongTinSD.getThietbi()).orElse(null);

//            Chỉ lấy thiết bị có thời gian trả là null và thời gian mượn là khác null
                if (thietBi != null && thongTinSD.getTGMuon() != null && thongTinSD.getTGTra() == null) {
                    // Tạo map để lưu thông tin của mỗi thiết bị và thời gian mượn tương ứng
                    Map<String, Object> deviceAndUsageInfo = new HashMap<>();
                    deviceAndUsageInfo.put("tenTB", thietBi.getTenTB());
                    deviceAndUsageInfo.put("moTaTB", thietBi.getMoTaTB());
                    deviceAndUsageInfo.put("TGMuon", thongTinSD.getTGMuon());

                    // Thêm map vào danh sách
                    thietbiList.add(deviceAndUsageInfo);
                }
            }
        }

        // Thêm danh sách vào model
        model.addAttribute("thietbiList", thietbiList);

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

        // Lấy danh sách thông tin sử dụng của user hiện tại
        List<ThongTinSD> thongTinSDList = thongTinSDRepository.findByThanhvien(memberID);

        // Tạo danh sách để lưu thông tin cần thiết
        List<Map<String, Object>> thietbiList = new ArrayList<>();

        for (ThongTinSD thongTinSD : thongTinSDList) {
//            Nếu thông tin sử dụng có chứa thiết bị
            if (thongTinSD.getThietbi() != null) {
                // Tìm thiết bị tương ứng
                ThietBi thietBi = thietBiRepository.findById(thongTinSD.getThietbi()).orElse(null);

//            Chỉ lấy thiết bị đang đặt (thời gian trả = null, thời gian mượn = null)
                if (thietBi != null
                        && thongTinSD.getTGMuon() == null
                        && thongTinSD.getTGTra() == null
                        && thongTinSD.getTGDatCho() != null) {
                    // Tạo map để lưu thông tin của mỗi thiết bị và thời gian mượn tương ứng
                    Map<String, Object> deviceAndUsageInfo = new HashMap<>();
                    deviceAndUsageInfo.put("tenTB", thietBi.getTenTB());
                    deviceAndUsageInfo.put("moTaTB", thietBi.getMoTaTB());
                    deviceAndUsageInfo.put("TGDat", thongTinSD.getTGDatCho());

                    // Thêm map vào danh sách
                    thietbiList.add(deviceAndUsageInfo);
                }
            }
        }

        // Thêm danh sách vào model
        model.addAttribute("thietbiList", thietbiList);

        return "user-booking-status";
    }
}

package com.example.membermanagerment.controller;

import com.example.membermanagerment.model.ThanhVien;
import com.example.membermanagerment.model.ThietBi;
import com.example.membermanagerment.model.ThongTinSD;
import com.example.membermanagerment.model.XuLy;
import com.example.membermanagerment.repository.ThanhVienRepository;
import com.example.membermanagerment.repository.ThietBiRepository;
import com.example.membermanagerment.repository.ThongTinSDRepository;
import com.example.membermanagerment.repository.XuLyRepository;
import com.example.membermanagerment.utilities.ExcelUtil;
import com.example.membermanagerment.utilities.thanhvienExcelUtil;
import com.example.membermanagerment.utilities.thietbiExcelUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class AdminController {
    @Autowired
    private ThietBiRepository thietBiRepository;

    @Autowired
    private ThongTinSDRepository thongtinSdRepository;

    @Autowired
    private ThanhVienRepository thanhVienRepository;

    @Autowired
    private XuLyRepository xuLyRepository;

    @GetMapping("/admin")
    public String admin(Model model) {

        // show list thanhvien with TGVao != null
        List<ThongTinSD> list = new ArrayList<>();
        for (ThongTinSD info : thongtinSdRepository.findAll()) {
            if (info.getTGVao() != null) {
                ThanhVien thanhvien = thanhVienRepository.findByMaTV(info.getThanhvien());
                if (thanhvien != null) {
                    info.setMember(thanhvien);
                }
                list.add(info);
            }
        }
        model.addAttribute("checkInList", list);

        // show total member + total member with TGVao != null
        int totalMember = (int) thanhVienRepository.count();
        int totalMemberWithTGVao = 0;
        for (ThongTinSD info : thongtinSdRepository.findAll()) {
            if (info.getTGVao() != null) {
                totalMemberWithTGVao++;
            }
        }
        model.addAttribute("totalMember", totalMember);
        model.addAttribute("totalMemberWithTGVao", totalMemberWithTGVao);

        return "admin";
    }

    // --------------------MEMBER----------------------
    @GetMapping("/admin-thanhvien")
    public String admin_thanhvien(Model model) {
        List<ThanhVien> memberList = thanhVienRepository.findAll();
        model.addAttribute("memberList", memberList);
        return "admin-thanhvien";
    }

    @PostMapping("/addMember")
    @ResponseBody
    public Map<String, Object> addMember(@RequestBody Map<String, String> memberData) {
        Map<String, Object> response = new HashMap<>();

        if (memberData.get("maTV") == null || memberData.get("maTV").isEmpty() ||
                memberData.get("tenTV") == null || memberData.get("tenTV").isEmpty() ||
                memberData.get("khoa") == null || memberData.get("khoa").isEmpty() ||
                memberData.get("nganh") == null || memberData.get("nganh").isEmpty() ||
                memberData.get("sdt") == null || memberData.get("sdt").isEmpty() ||
                memberData.get("password") == null || memberData.get("password").isEmpty() ||
                memberData.get("email") == null || memberData.get("email").isEmpty()) {

            response.put("success", false);
            response.put("message", "Không được để trống các trường");
            return response;
        }

        BigInteger maTV = new BigInteger(memberData.get("maTV"));
        String tenTV = memberData.get("tenTV");
        String khoa = memberData.get("khoa");
        String nganh = memberData.get("nganh");
        String sdt = memberData.get("sdt");
        String password = memberData.get("password");
        String email = memberData.get("email");
        String gmailRegex = "^[a-zA-Z0-9_]+@gmail\\.com$";

        if (!email.matches(gmailRegex)) {
            response.put("success", false);
            response.put("message", "Email không đúng định dạng Gmail");
            return response;
        }

        ThanhVien existingMemberByMaTV = thanhVienRepository.findByMaTV(maTV);
        ThanhVien existingMemberByEmail = thanhVienRepository.findByEmail(email);
        ThanhVien existingMemberBySdt = thanhVienRepository.findBySdt(sdt);
        ThanhVien existingMemberByTenTV = thanhVienRepository.findByHoTen(tenTV);

        if (existingMemberByMaTV != null && !existingMemberByMaTV.getMaTV().equals(maTV)) {
            response.put("message", "Mã thành viên đã tồn tại");
            response.put("success", false);
            return response;
        }
        if (existingMemberByEmail != null && !existingMemberByEmail.getEmail().equals(email)) {
            response.put("message", "Email đã tồn tại");
            response.put("success", false);
            return response;
        }
        if (existingMemberBySdt != null && !existingMemberBySdt.getSdt().equals(sdt)) {
            response.put("message", "Số điện thoại đã tồn tại");
            response.put("success", false);
            return response;
        }
        if (existingMemberByTenTV != null && !existingMemberByTenTV.getHoTen().equals(tenTV)) {
            response.put("message", "Tên thành viên đã tồn tại");
            response.put("success", false);
            return response;
        }

        ThanhVien newMember = new ThanhVien(maTV, tenTV, khoa, nganh, sdt, password, email);
        ThanhVien addedMember = thanhVienRepository.save(newMember);

        response.put("success", addedMember != null);
        if (addedMember != null) {
            response.put("message", "Thêm thành viên thành công");
        }
        return response;
    }

    @PostMapping("/editMember")
    @ResponseBody
    public Map<String, Object> editMember(@RequestBody Map<String, String> memberData) {
        Map<String, Object> response = new HashMap<>();

        if (memberData.get("maTV") == null || memberData.get("maTV").isEmpty() ||
                memberData.get("tenTV") == null || memberData.get("tenTV").isEmpty() ||
                memberData.get("khoa") == null || memberData.get("khoa").isEmpty() ||
                memberData.get("nganh") == null || memberData.get("nganh").isEmpty() ||
                memberData.get("sdt") == null || memberData.get("sdt").isEmpty() ||
                memberData.get("password") == null || memberData.get("password").isEmpty() ||
                memberData.get("email") == null || memberData.get("email").isEmpty()) {

            response.put("success", false);
            response.put("message", "Không được để trống các trường");
            return response;
        }

        BigInteger maTV = new BigInteger(memberData.get("maTV"));
        String tenTV = memberData.get("tenTV");
        String khoa = memberData.get("khoa");
        String nganh = memberData.get("nganh");
        String sdt = memberData.get("sdt");
        String password = memberData.get("password");
        String email = memberData.get("email");
        String gmailRegex = "^[a-zA-Z0-9_]+@gmail\\.com$";

        if (!email.matches(gmailRegex)) {
            System.out.println("kaka");
            response.put("success", false);
            response.put("message", "Email không đúng định dạng Gmail");
            return response;
        }

        ThanhVien existingMemberByMaTV = thanhVienRepository.findByMaTV(maTV);
        ThanhVien existingMemberByEmail = thanhVienRepository.findByEmail(email);
        ThanhVien existingMemberBySdt = thanhVienRepository.findBySdt(sdt);
        ThanhVien existingMemberByTenTV = thanhVienRepository.findByHoTen(tenTV);

        if (existingMemberByMaTV != null && !existingMemberByMaTV.getMaTV().equals(maTV)) {
            response.put("message", "Mã thành viên đã tồn tại");
            response.put("success", false);
            return response;
        }
        if (existingMemberByEmail != null && !existingMemberByEmail.getMaTV().equals(maTV)) {
            response.put("message", "Email đã tồn tại");
            response.put("success", false);
            return response;
        }
        if (existingMemberBySdt != null && !existingMemberBySdt.getMaTV().equals(maTV)) {
            response.put("message", "Số điện thoại đã tồn tại");
            response.put("success", false);
            return response;
        }
        if (existingMemberByTenTV != null && !existingMemberByTenTV.getMaTV().equals(maTV)) {
            response.put("message", "Tên thành viên đã tồn tại");
            response.put("success", false);
            return response;
        }

        ThanhVien updateMember = new ThanhVien(maTV, tenTV, khoa, nganh, sdt, password, email);
        ThanhVien result = thanhVienRepository.save(updateMember);

        response.put("success", result != null);
        if (result != null) {
            response.put("message", "Cập nhật thành viên thành công");
        }
        return response;
    }

    @PostMapping("/getMember")
    @ResponseBody
    @JsonIgnoreProperties
    public ThanhVien getMemberById(@RequestBody Map<String, String> requestData) {
        BigInteger maTV = new BigInteger(requestData.get("maTV"));
        ThanhVien thanhvien = thanhVienRepository.findByMaTV(maTV);
        return thanhvien;
    }

    @PostMapping("/deleteMember")
    @ResponseBody
    public Map<String, Object> deleteMemberById(@RequestBody Map<String, String> memberData) {
        Map<String, Object> response = new HashMap<>();
        BigInteger maTV = new BigInteger(memberData.get("maTV"));

        ThanhVien thanhVien = thanhVienRepository.findByMaTV(maTV);
        if (thanhVien == null) {
            response.put("success", false);
            response.put("message", "Không tồn tại thành viên này");
            return response;
        }

        try {
            thanhVienRepository.delete(thanhVien);
            response.put("success", true);
            response.put("message", "Xóa thành công");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Xóa thất bại");
        }
        return response;
    }

    // search member
    @PostMapping("/searchMember")
    @ResponseBody
    public List<ThanhVien> searchMember(@RequestBody Map<String, String> searchData) {
        String searchValue = searchData.get("searchValue");
        if (searchValue.isEmpty()) {
            return thanhVienRepository.findAll();
        }
        System.out.println(thanhVienRepository.findByKeyword(searchValue));
        return thanhVienRepository.findByKeyword(searchValue);
    }

    // search member by year
    @PostMapping("/searchMemberByYear")
    @ResponseBody
    public List<ThanhVien> searchMemberByYear(@RequestBody Map<String, String> searchData) {
        String searchValue = searchData.get("searchValue");
        if (searchValue == null || searchValue.isEmpty()) {
            return thanhVienRepository.findAll();
        }
        return thanhVienRepository.findByYear(searchValue);
    }

    @PostMapping("/deleteAll")
    @ResponseBody
    public Map<String, Object> deleteAll(@RequestBody Map<String, String> searchData) {
        Map<String, Object> response = new HashMap<>();

        String searchValue = searchData.get("searchValue");
        List<ThanhVien> deletedMembers = thanhVienRepository.findByYear(searchValue);

        if (deletedMembers.isEmpty()) {
            response.put("success", false);
            response.put("message", "Không tồn tại thành viên nào để xóa");
            return response;
        }

        thanhVienRepository.deleteAll(deletedMembers);
        response.put("success", true);
        response.put("message", "Xóa thành công ");
        return response;
    }

    // --------------------END MEMBER----------------------
    // --------------------DEVICE----------------------
    @GetMapping("/admin-thietbi")
    public String admin_thietbi(Model model) {
        List<ThietBi> thietBiList = thietBiRepository.findAll();
        model.addAttribute("thietBiList", thietBiList);
        return "admin-thietbi";
    }

    @PostMapping("/addDevice")
    @ResponseBody
    public Map<String, Object> addDevice(@RequestBody Map<String, String> deviceData) {
        Map<String, Object> response = new HashMap<>();

        String tenTB = deviceData.get("tenThietBi");
        String moTa = deviceData.get("moTa");

        ThietBi existingDevice = thietBiRepository.findByTenTB(tenTB);
        if (existingDevice != null) {
            response.put("success", false);
            response.put("message", "Thiết bị đã tồn tại");
            return response;
        }

        ThietBi newDevice = new ThietBi(thietBiRepository.getMax(), tenTB, moTa);
        ThietBi addedDevice = thietBiRepository.save(newDevice);

        response.put("success", addedDevice != null);
        if (addedDevice != null) {
            response.put("message", "Thêm thiết bị thành công");
        }
        return response;
    }

    @PostMapping("/editDevice")
    @ResponseBody
    public Map<String, Object> editDevice(@RequestBody Map<String, String> deviceData) {
        Map<String, Object> response = new HashMap<>();

        int maTB = Integer.parseInt(deviceData.get("maTB"));
        String tenTB = deviceData.get("tenThietBi");
        String moTa = deviceData.get("moTa");

        ThietBi existingDevice = thietBiRepository.findByTenTB(tenTB);
        if (existingDevice != null && existingDevice.getMaTB() != maTB) {
            response.put("success", false);
            response.put("message", "Tên thiết bị đã tồn tại");
            return response;
        }

        ThietBi updateDevice = new ThietBi(maTB, tenTB, moTa);
        ThietBi result = thietBiRepository.save(updateDevice);

        response.put("success", result != null);
        if (result != null) {
            response.put("message", "Cập nhật thiết bị thành công");
        }
        return response;
    }

    @PostMapping("/getDevice")
    @ResponseBody
    @JsonIgnoreProperties
    public ThietBi getDeviceById(@RequestBody Map<String, Integer> requestData) {
        int maTB = requestData.get("maTB");
        ThietBi thietbi = thietBiRepository.findById(maTB).orElse(null);
        return thietbi;
    }

    @PostMapping("/deleteDevice")
    @ResponseBody
    @JsonIgnoreProperties
    public Map<String, Object> deleteDeviceById(@RequestBody Map<String, Integer> requestData) {
        Map<String, Object> response = new HashMap<>();
        int maTB = requestData.get("maTB");

        ThietBi thietBi = thietBiRepository.findById(maTB).orElse(null);
        if (thietBi == null) {
            response.put("success", false);
            response.put("message", "Không tồn tại thiết bị này");
            return response;
        }

        try {
            thietBiRepository.delete(thietBi);
            response.put("success", true);
            response.put("message", "Xóa thành công");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Xóa thất bại");
        }
        return response;
    }

    // search device
    @PostMapping("/searchDevice")
    @ResponseBody
    @JsonIgnoreProperties
    public List<ThietBi> searchDevice(@RequestBody Map<String, String> searchData) {
        String searchValue = searchData.get("searchValue");
        if (searchValue.isEmpty()) {
            return thietBiRepository.findAll();
        }
        List<ThietBi> searchResult = thietBiRepository.findByKeyword(searchValue);
        return searchResult;
    }

    // --------------------END DEVICE----------------------
    // --------------------VI PHAM----------------------
    @GetMapping("/admin-vipham")
    public String admin_vipham(Model model) {
        List<XuLy> errorList = new ArrayList<>();
        for (XuLy error : xuLyRepository.findAll()) {
            ThanhVien thanhvien = thanhVienRepository.findByMaTV(error.getMaTV());
            error.setThanhvien(thanhvien);
            errorList.add(error);
        }
        model.addAttribute("errorList", errorList);

        List<Map<String, Object>> thanhVienList = new ArrayList<>();

        for (ThanhVien tv : thanhVienRepository.findAll()) {
            Map<String, Object> thanhVien = new HashMap<>();
            thanhVien.put("maTV", tv.getMaTV());
            thanhVien.put("tenTV", tv.getHoTen());

            thanhVienList.add(thanhVien);
        }
        model.addAttribute("thanhVienList", thanhVienList);

        return "admin-vipham";
    }

    @PostMapping("/addError")
    @ResponseBody
    public Map<String, Object> addError(@RequestBody Map<String, String> errorData) {
        Map<String, Object> response = new HashMap<>();

        if (errorData.get("maTV") == null || errorData.get("maTV").isEmpty() ||
                errorData.get("hinhthuc") == null || errorData.get("hinhthuc").isEmpty() ||
                errorData.get("ngayxuly") == null || errorData.get("ngayxuly").isEmpty()) {
            response.put("success", false);
            response.put("message", "Không được để trống các trường hoặc trạng thái không hợp lệ");
            return response;
        }

        BigInteger maTV = new BigInteger(errorData.get("maTV"));
        String hinhthuc = errorData.get("hinhthuc");
        Timestamp ngayxuly = Timestamp.valueOf(errorData.get("ngayxuly"));
        int status = Integer.parseInt(errorData.get("status"));
        String sotienStr = errorData.get("sotien");
        int sotien = 0;

        ThanhVien curMem = thanhVienRepository.findByMaTV(maTV);
        if (curMem == null) {
            response.put("success", false);
            response.put("message", "Không tồn tại thành viên này");
            return response;
        }

        for (XuLy xuly : xuLyRepository.findAll()) {
            if (xuly.getHinhThucXL().equals(hinhthuc) && xuly.getTrangThaiXL() == 0) {
                response.put("success", false);
                response.put("message", "Thành viên đang có vi phạm này chưa được xử lý");
                return response;
            }
        }

        if (hinhthuc.contains("Khóa thẻ")) {
            if (hinhthuc.contains("bồi thường")) {
                try {
                    sotien = Integer.parseInt(sotienStr);
                } catch (NumberFormatException e) {
                    response.put("success", false);
                    response.put("message", "Số tiền phải là kiểu số nguyên");
                    return response;
                }
                XuLy newXuly = new XuLy(xuLyRepository.getMax(), maTV, hinhthuc, sotien, ngayxuly, status);
                XuLy addedXuly = xuLyRepository.save(newXuly);

                response.put("success", addedXuly != null);
                if (addedXuly != null) {
                    response.put("message", "Thêm xử lý thành công");
                }
                return response;
            } else {
                XuLy newXuly = new XuLy(xuLyRepository.getMax(), maTV, hinhthuc, null, ngayxuly, status);
                XuLy addedXuly = xuLyRepository.save(newXuly);

                response.put("success", addedXuly != null);
                if (addedXuly != null) {
                    response.put("message", "Thêm xử lý thành công");
                }
                return response;
            }
        } else {
            try {
                sotien = Integer.parseInt(sotienStr);
            } catch (NumberFormatException e) {
                response.put("success", false);
                response.put("message", "Số tiền phải là kiểu số nguyên");
                return response;
            }
            XuLy newXuly = new XuLy(xuLyRepository.getMax(), maTV, hinhthuc, sotien, ngayxuly, status);
            XuLy addedXuly = xuLyRepository.save(newXuly);

            response.put("success", addedXuly != null);
            if (addedXuly != null) {
                response.put("message", "Thêm xử lý thành công");
            }
            return response;
        }
    }

    @PostMapping("/getError")
    @ResponseBody
    @JsonIgnoreProperties
    public XuLy getErrorById(@RequestBody Map<String, Integer> requestData) {
        int maXL = requestData.get("maXL");
        XuLy xuly = xuLyRepository.findById(maXL).orElse(null);
        System.out.println("coi thu: " + xuly.toString());
        return xuly;
    }

    @PostMapping("/editError")
    @ResponseBody
    public Map<String, Object> editError(@RequestBody Map<String, String> requestData) {
        Map<String, Object> response = new HashMap<>();

        if (requestData.get("maTV") == null || requestData.get("maTV").isEmpty() ||
                requestData.get("hinhthuc") == null || requestData.get("hinhthuc").isEmpty() ||
                requestData.get("ngayxuly") == null || requestData.get("ngayxuly").isEmpty()) {
            response.put("success", false);
            response.put("message", "Không được để trống các trường hoặc trạng thái không hợp lệ");
            return response;
        }
        if (requestData.get("maTV") == null || requestData.get("maTV").isEmpty()) {
            response.put("success", false);
            response.put("message", "Không được để trống các trường hoặc trạng thái không hợp lệ");
            return response;
        }

        int maXL = Integer.parseInt(requestData.get("maXL"));
        BigInteger maTV = new BigInteger(requestData.get("maTV"));
        String hinhthuc = requestData.get("hinhthuc");
        Timestamp ngayxuly = Timestamp.valueOf(requestData.get("ngayxuly"));
        int status = Integer.parseInt(requestData.get("status"));
        String sotienStr = requestData.get("sotien");
        int sotien = 0;

        ThanhVien curMem = thanhVienRepository.findByMaTV(maTV);
        if (curMem == null) {
            response.put("success", false);
            response.put("message", "Không tồn tại thành viên này");
            return response;
        }

        XuLy xuly = xuLyRepository.findById(maXL).orElse(null);
        if (xuly.getTrangThaiXL() == 1) {
            response.put("success", false);
            response.put("message", "Trạng thái đã được xử lý, không thể điều chỉnh được nữa!!!");
            return response;
        }

        for (XuLy xuly1 : xuLyRepository.findAll()) {
            if (xuly1.getHinhThucXL().equals(hinhthuc) && xuly1.getTrangThaiXL() == 0 && xuly1.getMaXL() != maXL) {
                response.put("success", false);
                response.put("message", "Thành viên đang có vi phạm này chưa được xử lý");
                return response;
            }
        }

        if (hinhthuc.contains("Khóa thẻ")) {
            if (hinhthuc.contains("bồi thường")) {
                try {
                    sotien = Integer.parseInt(sotienStr);
                } catch (NumberFormatException e) {
                    response.put("success", false);
                    response.put("message", "Số tiền phải là kiểu số nguyên");
                    return response;
                }
                XuLy curXuly = new XuLy(maXL, maTV, hinhthuc, sotien, ngayxuly, status);
                XuLy updateXuly = xuLyRepository.save(curXuly);

                response.put("success", updateXuly != null);
                if (updateXuly != null) {
                    response.put("message", "Sửa xử lý thành công");
                }
                return response;
            } else {
                XuLy curXuly = new XuLy(maXL, maTV, hinhthuc, null, ngayxuly, status);
                XuLy updateXuly = xuLyRepository.save(curXuly);

                response.put("success", updateXuly != null);
                if (updateXuly != null) {
                    response.put("message", "Sửa xử lý thành công");
                }
                return response;
            }
        } else {
            try {
                sotien = Integer.parseInt(sotienStr);
            } catch (NumberFormatException e) {
                response.put("success", false);
                response.put("message", "Số tiền phải là kiểu số nguyên");
                return response;
            }
            XuLy curXuly = new XuLy(maXL, maTV, hinhthuc, sotien, ngayxuly, status);
            XuLy updateXuly = xuLyRepository.save(curXuly);

            response.put("success", updateXuly != null);
            if (updateXuly != null) {
                response.put("message", "Sửa xử lý thành công");
            }
            return response;
        }
    }

    @PostMapping("/deleteError")
    @ResponseBody
    @JsonIgnoreProperties
    public Map<String, Object> deleteErrorById(@RequestBody Map<String, Integer> requestData) {
        Map<String, Object> response = new HashMap<>();
        int maXL = requestData.get("maXL");

        XuLy xuly = xuLyRepository.findById(maXL).orElse(null);
        if (xuly == null) {
            response.put("success", false);
            response.put("message", "Không tồn tại thiết bị này");
            return response;
        }

        try {
            xuLyRepository.delete(xuly);
            response.put("success", true);
            response.put("message", "Xóa thành công");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Xóa thất bại");
        }
        return response;
    }

    @PostMapping("/searchError")
    @ResponseBody
    @JsonIgnoreProperties
    public List<XuLy> searchError(@RequestBody Map<String, String> searchData) {
        String searchValue = searchData.get("searchValue");
        if (searchValue.isEmpty()) {
            return xuLyRepository.findAll();
        }
        List<XuLy> searchResult = xuLyRepository.findByKeyword(searchValue);
        return searchResult;
    }

    @PostMapping("/uploadExcel")
    @ResponseBody
    public Map<String, Object> handleFileUpload(@RequestParam("excelBtn") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        if (!file.isEmpty()) {
            try {
                // Convert MultipartFile to File
                File convertFile = new File(file.getOriginalFilename());
                convertFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(convertFile);
                fos.write(file.getBytes());
                fos.close();

                List<List<String>> data = ExcelUtil.readExcel(convertFile.getAbsolutePath(), 0);

                List<ThanhVien> thanhvienList = thanhvienExcelUtil.convertTothanhvienList(data);
                // clear all data in table thanhvien
                thanhVienRepository.deleteAll();
                thanhvienList.forEach(thanhvien -> thanhVienRepository.save(thanhvien));

                convertFile.delete();

                response.put("success", true);
                response.put("message", "File uploaded and processed successfully");

                return response;
            } catch (Exception e) {
                e.printStackTrace();
                response.put("success", false);
                response.put("message", "An error occurred while processing the file");
                return response;
            }
        }
        response.put("success", false);
        response.put("message", "No file uploaded");
        return response;
    }

    @PostMapping("/uploadDeviceExcel")
    @ResponseBody
    public Map<String, Object> handleDeviceFileUpload(@RequestParam("excelDeviceBtn") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        if (!file.isEmpty()) {
            try {
                // Convert MultipartFile to File
                File convertFile = new File(file.getOriginalFilename());
                convertFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(convertFile);
                fos.write(file.getBytes());
                fos.close();

                List<List<String>> data = ExcelUtil.readExcel(convertFile.getAbsolutePath(), 0);

                List<ThietBi> thietbiList = thietbiExcelUtil.convertToThietbiList(data);
                // clear all data in table thietbi
                thietBiRepository.deleteAll();
                thietbiList.forEach(thietbi -> thietBiRepository.save(thietbi));

                convertFile.delete();

                response.put("success", true);
                response.put("message", "File uploaded and processed successfully");

                return response;
            } catch (Exception e) {
                e.printStackTrace();
                response.put("success", false);
                response.put("message", "An error occurred while processing the file");
                return response;
            }
        }
        response.put("success", false);
        response.put("message", "No file uploaded");
        return response;
    }

    public Boolean isDatChoByMaTB(Integer maTB) {
        List<ThongTinSD> thongTinSdList = thongtinSdRepository.findAll();
        for (ThongTinSD tt : thongTinSdList) {
            // Nếu có thiết bị trong db
            if (tt.getThietbi() == maTB && tt.getTGDatCho() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime tg_datcho = LocalDateTime.parse(tt.getTGDatCho().toString(), formatter);
                if (tg_datcho.isAfter(LocalDateTime.now().minusHours(1)))
                    return true;
            }
        }
        // Nếu không tìm thấy mã thiết bị
        return false;
    }

    public boolean isDatChoYourSelf(BigInteger maTV, Integer maTB) {
        // mình đang đặt chỗ
        for (ThongTinSD tt : thongtinSdRepository.findAll()) {
            if (tt.getThanhvien() == maTV && tt.getThietbi() == maTB && tt.getTGDatCho() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime tg_datcho = LocalDateTime.parse(tt.getTGDatCho().toString(), formatter);
                if (tg_datcho.isAfter(LocalDateTime.now().minusHours(1)))
                    return true;
            }
        }
        return false;
    }

    public boolean checkMuon(Integer maTB) {
        for (ThongTinSD tt : thongtinSdRepository.findAll()) {
            if (tt.getThietbi() == maTB) {
                if (isDatChoByMaTB(maTB) || (tt.getTGMuon() != null && tt.getTGTra() == null)) {
                    // có ai đang đặt chỗ hoặc đang mượn thì ko được mượn
                    return false;
                }
            }
        }
        // Thiết bị đang trống có thể mượn
        return true;
    }

    public boolean checkTra(BigInteger maTV, Integer maTB) {
        for (ThongTinSD tt : thongtinSdRepository.findAll()) {
            if (tt.getThanhvien() == maTV && tt.getThietbi() == maTB) {
                if (tt.getTGMuon() != null && tt.getTGTra() == null) {
                    return true;
                }
            }
        }
        return false;
    }

    @GetMapping("/getBookedDevicesFromUser")
    @ResponseBody
    public Map<BigInteger, List<ThongTinSD>> getBookedDevices() {
        System.out.println("Getting all ThongTinSD");
        List<ThongTinSD> thongTinSdList = thongtinSdRepository.findAll();
        System.out.println("Got " + thongTinSdList.size() + " ThongTinSD");

        Map<BigInteger, List<ThongTinSD>> bookedDevices = new HashMap<>();

        for (ThongTinSD tt : thongTinSdList) {
            BigInteger maTV = tt.getThanhvien();
            if (isDatChoYourSelf(maTV, tt.getThietbi())) {
                List<ThongTinSD> list = bookedDevices.get(maTV);
                if (list == null) {
                    list = new ArrayList<>();
                    bookedDevices.put(maTV, list);
                }
                list.add(tt);
            }
        }

        System.out.println("Returning bookedDevices");
        return bookedDevices;
    }

    @PostMapping("/checkError")
    @ResponseBody
    @JsonIgnoreProperties
    public Map<String, Object> getXulyByMaSV(@RequestBody Map<String, String> warningData) {
        Map<String, Object> response = new HashMap<>();
        BigInteger maTV = new BigInteger(warningData.get("maTV"));
        List<XuLy> list = xuLyRepository.findByMaTVAndTrangThaiXL(maTV);
        if (list.isEmpty()) {
            response.put("success", false);
            response.put("message", "Thành viên này chưa có vi phạm");
        } else {
            response.put("success", true);
        }
        return response;
    }

    @PostMapping("/warningMember")
    @ResponseBody
    public Map<String, Object> warningMember(@RequestBody Map<String, String> warningData) {
        Map<String, Object> response = new HashMap<>();

        BigInteger maTV = new BigInteger(warningData.get("maTV"));
        List<XuLy> list = xuLyRepository.findByMaTVAndTrangThaiXL(maTV);

        response.put("success", true);
        response.put("data", list);
        return response;
    }

    @PostMapping("/checkIn")
    @ResponseBody
    public Map<String, Object> checkInMember(@RequestBody Map<String, String> memberData) {
        Map<String, Object> response = new HashMap<>();

        BigInteger maTV = new BigInteger(memberData.get("maTV"));
        ThanhVien thanhVien = thanhVienRepository.findByMaTV(maTV);

        if (thanhVien == null) {
            response.put("success", false);
            response.put("message", "Không tìm thấy thành viên");
            return response;
        }
        ThongTinSD info = new ThongTinSD(thanhVien.getMaTV(), null, new Timestamp(System.currentTimeMillis()), null,
                null, null);
        ThongTinSD result = thongtinSdRepository.save(info);

        if (result == null) {
            response.put("success", false);
            response.put("message", "Check-in thất bại");
            return response;
        }

        response.put("success", true);
        response.put("data", thanhVien);
        return response;
    }

    @PostMapping("/searchCheckIn")
    @ResponseBody
    @JsonIgnoreProperties
    public Map<String, Object> searchCheckIn(@RequestBody Map<String, String> requestData) {
        Map<String, Object> response = new HashMap<>();

        String fromDateString = requestData.get("fromDate");
        String toDateString = requestData.get("toDate");
        String khoa = requestData.get("khoa");
        String nganh = requestData.get("nganh");

        LocalDateTime now = LocalDateTime.now();
        int currentSecond = now.getSecond();

        if (fromDateString.isEmpty() && toDateString.isEmpty()) {
            fromDateString = "1970-01-01 00:00";
            toDateString = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        } else {
            if (fromDateString.isEmpty()) {
                fromDateString = "1970-01-01 00:00";
            }

            if (toDateString.isEmpty()) {
                toDateString = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
        }

        fromDateString += ":" + currentSecond;
        toDateString += ":" + currentSecond;

        Timestamp fromDate = Timestamp.valueOf(fromDateString);
        Timestamp toDate = Timestamp.valueOf(toDateString);

        List<ThongTinSD> result = thongtinSdRepository.findByKeywordCheckIn(fromDate, toDate, khoa, nganh);
        System.out.println(result);
        if (result.isEmpty()) {
            response.put("success", false);
            response.put("message", "Không có sinh viên thỏa điều kiện");
            return response;
        }
        response.put("success", true);
        response.put("result", result);
        return response;
    }

    // Dashboard device availability
    @PostMapping("/getDeviceAvailability")
    @ResponseBody
    public Map<String, Object> getDeviceAvailability() {
        Map<Integer, String> deviceAvailabilityMap = new HashMap<>();
        Map<String, Object> response = new HashMap<>();
        List<ThietBi> thietBiList = thietBiRepository.findAll();
        List<ThongTinSD> thongTinSdList = thongtinSdRepository.findAll();

        Map<Integer, BigInteger> borrowedStudentId = new HashMap<>();
        Map<Integer, Timestamp> borrowedStartDate = new HashMap<>();
        Map<Integer, Timestamp> borrowedEndDate = new HashMap<>();
        for (ThongTinSD tt : thongTinSdList) {
            if (tt.getThietbi() != null) {
                if (tt.getTGMuon() != null && tt.getTGTra() != null) {
                    borrowedStudentId.put(tt.getThietbi(), tt.getThanhvien());
                    borrowedStartDate.put(tt.getThietbi(), tt.getTGMuon());
                    borrowedEndDate.put(tt.getThietbi(), tt.getTGTra());
                    deviceAvailabilityMap.put(tt.getThietbi(), "Đang mượn");
                }
            }
        }

        List<Map<String, String>> deviceList = new ArrayList<>();
        for (ThietBi tb : thietBiList) {
            Map<String, String> deviceInfo = new HashMap<>();
            deviceInfo.put("maTB", String.valueOf(tb.getMaTB()));
            deviceInfo.put("name", tb.getTenTB());
            deviceInfo.put("description", tb.getMoTaTB());
            deviceInfo.put("availability", deviceAvailabilityMap.getOrDefault(tb.getMaTB(), "Chưa mượn"));
            String studentId = "Không";
            if (borrowedStudentId.containsKey(tb.getMaTB())) {
                studentId = borrowedStudentId.get(tb.getMaTB()).toString();
                deviceInfo.put("TGMuon", borrowedStartDate.get(tb.getMaTB()).toString());
                deviceInfo.put("TGTra", borrowedEndDate.get(tb.getMaTB()).toString());
            }
            deviceInfo.put("studentId", studentId);
            deviceList.add(deviceInfo);
        }

        response.put("devices", deviceList);
        return response;
    }

    @PostMapping("/getThongTinSDList")
    @ResponseBody
    public Map<String, Object> getThongTinSD() {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, String>> thongtinsdInfoList = new ArrayList<>();
        List<ThongTinSD> thongTinSdList = thongtinSdRepository.findAll();
        for (ThongTinSD tt : thongTinSdList) {
            ThanhVien thanhVien = thanhVienRepository.findByMaTV(tt.getThanhvien());
            Map<String, String> thongtinsdInfo = new HashMap<>();
            thongtinsdInfo.put("maTV", thanhVien.getMaTV().toString());
            thongtinsdInfo.put("tenTV", thanhVien.getHoTen());
            thongtinsdInfo.put("tgVao", String.valueOf(tt.getTGVao()));
            thongtinsdInfoList.add(thongtinsdInfo);
        }
        response.put("success", true);
        response.put("data", thongtinsdInfoList);
        return response;
    }

    @PostMapping("/filterThongTinSD")
    @ResponseBody
    public Map<String, Object> filterThongTinSD(@RequestBody Map<String, String> filters) {
        Map<String, Object> response = new HashMap<>();
        String fromDate = filters.get("enterFromDate");
        String toDate = filters.get("enterToDate");
        String maTV = filters.get("inputMaTV");
        String tenTV = filters.get("inputTenTV");

        List<ThongTinSD> thongTinSdList = thongtinSdRepository.findAll();
        List<Map<String, String>> thongtinsdInfoList = new ArrayList<>();
        for (ThongTinSD tt : thongTinSdList) {
            ThanhVien thanhVien = thanhVienRepository.findByMaTV(tt.getThanhvien());
            if (thanhVien != null && thanhVien.getMaTV().toString().contains(maTV)
                    && thanhVien.getHoTen().contains(tenTV)) {
                if (fromDate.isEmpty() && toDate.isEmpty()) {
                    Map<String, String> thongtinsdInfo = new HashMap<>();
                    thongtinsdInfo.put("maTV", thanhVien.getMaTV().toString());
                    thongtinsdInfo.put("tenTV", thanhVien.getHoTen());
                    thongtinsdInfo.put("tgVao", tt.getTGVao() != null ? String.valueOf(tt.getTGVao()) : "N/A");
                    thongtinsdInfoList.add(thongtinsdInfo);
                } else {
                    if (fromDate.isEmpty()) {
                        fromDate = "1970-01-01 00:00:00";
                    }
                    if (toDate.isEmpty()) {
                        toDate = "9999-12-31 23:59:59";
                    }
                    if (tt.getTGVao() != null && tt.getTGVao().after(Timestamp.valueOf(fromDate))
                            && tt.getTGVao().before(Timestamp.valueOf(toDate))) {
                        Map<String, String> thongtinsdInfo = new HashMap<>();
                        thongtinsdInfo.put("maTV", thanhVien.getMaTV().toString());
                        thongtinsdInfo.put("tenTV", thanhVien.getHoTen());
                        thongtinsdInfo.put("tgVao", String.valueOf(tt.getTGVao()));
                        thongtinsdInfoList.add(thongtinsdInfo);
                    }
                }
            }
        }
        response.put("success", true);
        response.put("data", thongtinsdInfoList);
        return response;
    }

    @PostMapping("/getXuLy")
    @ResponseBody
    public Map<String, Object> getXuLyList() {
        Map<String, Object> response = new HashMap<>();
        List<XuLy> xuLyList = xuLyRepository.findAll();
        List<Map<String, String>> xuLyInfoList = new ArrayList<>();
        for (XuLy xl : xuLyList) {
            ThanhVien thanhVien = thanhVienRepository.findByMaTV(xl.getMaTV());
            Map<String, String> xuLyInfo = new HashMap<>();
            xuLyInfo.put("maXL", String.valueOf(xl.getMaXL()));
            xuLyInfo.put("maTV", thanhVien.getMaTV().toString());
            xuLyInfo.put("tenTV", thanhVien.getHoTen());
            xuLyInfo.put("hinhThuc", xl.getHinhThucXL());
            xuLyInfo.put("soTien", xl.getSoTien() != null ? String.valueOf(xl.getSoTien()) : "N/A");
            xuLyInfo.put("ngayXuLy", String.valueOf(xl.getNgayXL()));
            xuLyInfo.put("trangThai", xl.getTrangThaiXL() == 0 ? "Đang xử lý" : "Đã xử lý");
            xuLyInfoList.add(xuLyInfo);
        }
        response.put("success", true);
        response.put("data", xuLyInfoList);
        return response;
    }
}

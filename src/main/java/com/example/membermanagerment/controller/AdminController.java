    package com.example.membermanagerment.controller;
    import com.example.membermanagerment.model.ThanhVien;
    import com.example.membermanagerment.model.ThietBi;
    import com.example.membermanagerment.model.XuLy;
    import com.example.membermanagerment.repository.ThanhVienRepository;
    import com.example.membermanagerment.repository.ThietBiRepository;
    import com.example.membermanagerment.repository.XuLyRepository;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.ResponseBody;

    import java.math.BigInteger;
    import java.sql.Timestamp;
    import java.util.*;

    @Controller
    public class AdminController {
        @Autowired
        private ThietBiRepository thietBiRepository;

        @Autowired
        private ThanhVienRepository thanhVienRepository;
        @Autowired
        private XuLyRepository xuLyRepository;

        @GetMapping("/admin")
        public String admin() {
            return "admin";
        }
        // --------------------MEMBER----------------------
        @GetMapping("/admin-thanhvien")
        public String admin_thanhvien(Model model) {
            List<ThanhVien> memberList = thanhVienRepository.findAll();
            model.addAttribute("memberList",memberList);
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

            ThanhVien newMember = new ThanhVien(maTV, tenTV, khoa, nganh, sdt, password, email);
            ThanhVien addedMember = thanhVienRepository.save(newMember);

            response.put("success", addedMember != null);
            if (addedMember != null) {
                response.put("message", "Thêm thành viên thành công");
            } else {
                response.put("message", "Thêm thành viên thất bại");
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
            } else {
                response.put("message", "Cập nhật thành viên thất bại");
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
            model.addAttribute("thietBiList",thietBiList);
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

            ThietBi newDevice = new ThietBi(thietBiRepository.getMax(),tenTB,moTa);
            ThietBi addedDevice = thietBiRepository.save(newDevice);

            response.put("success", addedDevice != null);
            if (addedDevice != null) {
                response.put("message", "Thêm thiết bị thành công");
            } else {
                response.put("message", "Thêm thiết bị thất bại");
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


            ThietBi updateDevice = new ThietBi(maTB,tenTB,moTa);
            ThietBi result = thietBiRepository.save(updateDevice);

            response.put("success", result != null);
            if (result != null) {
                response.put("message", "Cập nhật thiết bị thành công");
            } else {
                response.put("message", "Cập nhật thiết bị thất bại");
            }
            return response;
        }

        @PostMapping("/getDevice")
        @ResponseBody
        @JsonIgnoreProperties
        public ThietBi getDeviceById(@RequestBody Map<String, Integer> requestData){
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
                if(searchValue.isEmpty()) {
                    return thietBiRepository.findAll();
                }
                List<ThietBi> searchResult = thietBiRepository.findByKeyword(searchValue);
                return searchResult;
            }
        // --------------------END DEVICE----------------------
        // --------------------VI PHAM----------------------
        @GetMapping("/admin-vipham")
        public String admin_vipham(Model model) {
            List<XuLy> errorList = xuLyRepository.findAll();
            model.addAttribute("errorList",errorList);
            return "admin-vipham";
        }

        @PostMapping("/addError")
        @ResponseBody
        public Map<String, Object> addError(@RequestBody Map<String, String> errorData) {
            Map<String, Object> response = new HashMap<>();

            if (errorData.get("maTV") == null || errorData.get("maTV").isEmpty() ||
                    errorData.get("hinhthuc") == null || errorData.get("hinhthuc").isEmpty() ||
                    errorData.get("sotien") == null || errorData.get("sotien").isEmpty() ||
                    errorData.get("ngayxuly") == null || errorData.get("ngayxuly").isEmpty()) {
                response.put("success", false);
                response.put("message", "Không được để trống các trường hoặc trạng thái không hợp lệ");
                return response;
            }

            BigInteger maTV = new BigInteger(errorData.get("maTV"));
            String hinhthuc = errorData.get("hinhthuc");
            int sotien = Integer.parseInt(errorData.get("sotien"));
            Timestamp ngayxuly = Timestamp.valueOf(errorData.get("ngayxuly"));
            int status = Integer.parseInt(errorData.get("status"));

            ThanhVien curMem = thanhVienRepository.findByMaTV(maTV);
            if (curMem == null) {
                response.put("success", false);
                response.put("message", "Không tồn tại thành viên này");
                return response;
            }



            XuLy newXuly = new XuLy(xuLyRepository.getMax(),maTV,hinhthuc,sotien,ngayxuly,status);
            XuLy addedXuly = xuLyRepository.save(newXuly);

            response.put("success", addedXuly != null);
            if (addedXuly != null) {
                response.put("message", "Thêm xử lý thành công");
            } else {
                response.put("message", "Thêm xử lý thất bại");
            }
            return response;
        }

        @PostMapping("/getError")
        @ResponseBody
        @JsonIgnoreProperties
        public XuLy getErrorById(@RequestBody Map<String, Integer> requestData){
            int maXL = requestData.get("maXL");
            XuLy xuly = xuLyRepository.findById(maXL).orElse(null);
            System.out.println("coi thu: "+xuly.toString());
            return xuly;
        }

        @PostMapping("/editError")
        @ResponseBody
        public Map<String, Object> editError(@RequestBody Map<String, String> requestData) {
            Map<String, Object> response = new HashMap<>();

            if (requestData.get("maTV") == null || requestData.get("maTV").isEmpty() ||
                    requestData.get("hinhthuc") == null || requestData.get("hinhthuc").isEmpty() ||
                    requestData.get("sotien") == null || requestData.get("sotien").isEmpty() ||
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
            int sotien;
            Timestamp ngayxuly = Timestamp.valueOf(requestData.get("ngayxuly"));
            int status = Integer.parseInt(requestData.get("status"));

            try{
                sotien = Integer.parseInt(requestData.get("sotien"));
            }catch(Exception e) {
                response.put("success", false);
                response.put("message", "Tiền không đúng định dạng");
                return response;
            }

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
            XuLy curXuly = new XuLy(maXL,maTV,hinhthuc,sotien,ngayxuly,status);
            XuLy updateXuly = xuLyRepository.save(curXuly);

            response.put("success", updateXuly != null);
            if (updateXuly != null) {
                response.put("message", "Sửa xử lý thành công");
            } else {
                response.put("message", "Sửa xử lý thất bại");
            }
            return response;
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
            if(searchValue.isEmpty()) {
                return xuLyRepository.findAll();
            }
            List<XuLy> searchResult = xuLyRepository.findByKeyword(searchValue);
            return searchResult;
        }
    }

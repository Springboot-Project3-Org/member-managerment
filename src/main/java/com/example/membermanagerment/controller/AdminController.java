    package com.example.membermanagerment.controller;
    import com.example.membermanagerment.model.ThietBi;
    import com.example.membermanagerment.repository.ThietBiRepository;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.ResponseBody;

    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    @Controller
    public class AdminController {
        @Autowired
        private ThietBiRepository thietBiRepository;

        @GetMapping("/admin")
        public String admin() {
            return "admin";
        }
        @GetMapping("/admin-thanhvien")
        public String admin_thanhvien() {
            return "admin-thanhvien";
        }
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



        @GetMapping("/admin-vipham")
        public String admin_vipham() {
            return "admin-vipham";
        }
    }

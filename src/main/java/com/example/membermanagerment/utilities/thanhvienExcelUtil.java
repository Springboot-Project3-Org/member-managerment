package com.example.membermanagerment.utilities;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.membermanagerment.model.ThanhVien;

public class thanhvienExcelUtil {
    public static List<ThanhVien> convertTothanhvienList(List<List<String>> data) {
        List<ThanhVien> thanhVienList = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("0");
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            BigInteger id;
            try {
                String idString = row.get(0);
                if (idString.contains(".")) {
                    double idDouble = Double.parseDouble(idString);
                    idString = decimalFormat.format(idDouble);
                }
                id = new BigInteger(idString);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid integer value in input data", e);
            }
            String HoTen = row.get(1);
            String Khoa = row.get(2);
            String Nganh = row.get(3);
            String Sdt = row.get(4);
            // try {
            // if (row.get(4).contains(".")) {
            // float floatValue = Float.parseFloat(row.get(4));
            // int intValue = (int) floatValue;
            // Sdt = String.valueOf(intValue);
            // } else {
            // Sdt = row.get(4);
            // }
            // } catch (NumberFormatException e) {
            // throw new IllegalArgumentException("Invalid integer value in input data", e);
            // }
            String password = row.get(5);
            String email = row.get(6);

            ThanhVien model = new ThanhVien(id, HoTen, Khoa, Nganh, Sdt, password, email);
            thanhVienList.add(model);
        }

        return thanhVienList;
    }
}

package com.example.membermanagerment.utilities;

import java.util.ArrayList;
import java.util.List;

import com.example.membermanagerment.model.ThietBi;

public class thietbiExcelUtil {
    public static List<ThietBi> convertToThietbiList(List<List<String>> data) {
        List<ThietBi> thietbis = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {
            List<String> row = data.get(i);
            try {
                // int MaTB;
                // try {
                // if (row.get(0).contains("."))
                // MaTB = (int) Float.parseFloat(row.get(0));
                // else
                // MaTB = Integer.parseInt(row.get(0));
                // } catch (NumberFormatException e) {
                // throw new IllegalArgumentException("Invalid integer value in input data", e);
                // }
                String TenTB = row.get(1);
                String MoTaTB = row.get(2);

                ThietBi thietbi = new ThietBi(TenTB, MoTaTB);
                thietbis.add(thietbi);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return thietbis;
    }
}

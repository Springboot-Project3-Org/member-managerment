package com.example.membermanagerment.repository;

import com.example.membermanagerment.model.ThongTinSD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThongTinSDRepository extends JpaRepository<ThongTinSD, Integer> {
    @Query("select max(ttsd.MaTT) + 1 from ThongTinSD ttsd")
    Integer getMax();
    @Query("SELECT ttsd FROM ThongTinSD ttsd WHERE " +
            "ttsd.MaTT LIKE CONCAT('%', :keyword, '%') " +
            "OR ttsd.thanhvien LIKE CONCAT('%', :keyword, '%') " +
            "OR ttsd.thietbi LIKE CONCAT('%', :keyword, '%') " +
            "OR ttsd.TGVao LIKE CONCAT('%', :keyword, '%') " +
            "OR ttsd.TGMuon LIKE CONCAT('%', :keyword, '%') " +
            "OR ttsd.TGTra LIKE CONCAT('%', :keyword, '%') " +
            "OR ttsd.TGDatCho LIKE CONCAT('%', :keyword, '%') ")
    List<ThongTinSD> findByKeyword(String keyword);
}

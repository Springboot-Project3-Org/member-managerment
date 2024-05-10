package com.example.membermanagerment.repository;

import com.example.membermanagerment.model.XuLy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface XuLyRepository extends JpaRepository<XuLy, Integer> {
    @Query("select max(xl.MaXL) + 1 from XuLy xl")
    Integer getMax();

    @Query("SELECT xl FROM XuLy xl WHERE " +
            "xl.MaTV LIKE CONCAT('%', :keyword, '%') " +
            "OR xl.HinhThucXL LIKE CONCAT('%', :keyword, '%') " +
            "OR xl.SoTien LIKE CONCAT('%', :keyword, '%') " +
            "OR xl.NgayXL LIKE CONCAT('%', :keyword, '%') " +
            "OR xl.TrangThaiXL LIKE CONCAT('%', :keyword, '%') ")
    List<XuLy> findByKeyword(String keyword);

    @Query("SELECT xl FROM XuLy xl WHERE xl.MaTV = :maTV AND xl.TrangThaiXL = 0")
    List<XuLy> findByMaTVAndTrangThaiXL(@Param("maTV") BigInteger maTV);
}

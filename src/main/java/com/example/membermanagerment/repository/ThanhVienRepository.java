package com.example.membermanagerment.repository;

import com.example.membermanagerment.model.ThanhVien;
import com.example.membermanagerment.model.ThietBi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface ThanhVienRepository extends JpaRepository<ThanhVien, BigInteger> {
    ThanhVien findByMaTV(BigInteger maTV);
    ThanhVien findByHoTen(String hoTen);
    ThanhVien findByEmail(String email);
    ThanhVien findBySdt(String sdt);
    ThanhVien findByEmailAndPassword(String email, String password);
    ThanhVien findByMaTVAndPassword(BigInteger maTV, String password);
    @Query("SELECT tv FROM ThanhVien tv WHERE " +
            "tv.maTV LIKE CONCAT('%', :keyword, '%') " +
            "OR tv.hoTen LIKE CONCAT('%', :keyword, '%') " +
            "OR tv.khoa LIKE CONCAT('%', :keyword, '%') " +
            "OR tv.nganh LIKE CONCAT('%', :keyword, '%') " +
            "OR tv.sdt LIKE CONCAT('%', :keyword, '%') " +
            "OR tv.email LIKE CONCAT('%', :keyword, '%')")
    List<ThanhVien> findByKeyword(String keyword);

    @Query("SELECT tv FROM ThanhVien tv WHERE SUBSTRING(CAST(tv.maTV AS string), 3, 2) = :year")
    List<ThanhVien> findByYear(String year);

}

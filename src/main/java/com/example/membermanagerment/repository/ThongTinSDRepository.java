package com.example.membermanagerment.repository;

import com.example.membermanagerment.model.ThongTinSD;

import java.math.BigInteger;
import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThongTinSDRepository extends JpaRepository<ThongTinSD, Integer> {
    @Query("select max(ttsd.MaTT) + 1 from ThongTinSD ttsd")
    Integer getMax();

    // ThongTinSD findByThanhvien(BigInteger thanhvien);
    @Query("SELECT ttsd FROM ThongTinSD ttsd WHERE " +
            "ttsd.MaTT LIKE CONCAT('%', :keyword, '%') " +
            "OR ttsd.thanhvien LIKE CONCAT('%', :keyword, '%') " +
            "OR ttsd.thietbi LIKE CONCAT('%', :keyword, '%') " +
            "OR ttsd.TGVao LIKE CONCAT('%', :keyword, '%') " +
            "OR ttsd.TGMuon LIKE CONCAT('%', :keyword, '%') " +
            "OR ttsd.TGTra LIKE CONCAT('%', :keyword, '%') " +
            "OR ttsd.TGDatCho LIKE CONCAT('%', :keyword, '%') ")
    List<ThongTinSD> findByKeyword(String keyword);

    List<ThongTinSD> findByThanhvien(BigInteger thanhvien);
    // Kiem tra xem thiet bi da duoc dat cho hay chua
    @Query("SELECT ttsd FROM ThongTinSD ttsd WHERE ttsd.thietbi = :maTB AND DATE(ttsd.TGDatCho) = DATE(:TGDatCho)")
    List<ThongTinSD> findByMaTBAndTGDatCho(@Param("maTB") int maTB, @Param("TGDatCho") Timestamp TGDatCho);
    // Kiem tra xem thiet bi duoc tra hay chua
    @Query("SELECT ttsd FROM ThongTinSD ttsd WHERE ttsd.thietbi = :maTB AND ttsd.TGMuon IS NOT NULL AND ttsd.TGTra IS NULL")
    List<ThongTinSD> findTraThietBi(@Param("maTB") int maTB);

    @Query("SELECT s FROM ThongTinSD s JOIN s.member tv WHERE s.TGVao BETWEEN :from AND :to AND tv.khoa LIKE CONCAT('%', UPPER(:khoa), '%') AND tv.nganh LIKE CONCAT('%', UPPER(:nganh), '%')")
    List<ThongTinSD> findByKeywordCheckIn(@Param("from") Timestamp from, @Param("to") Timestamp to, @Param("khoa") String khoa, @Param("nganh") String nganh);
}

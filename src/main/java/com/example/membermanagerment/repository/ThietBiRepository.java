package com.example.membermanagerment.repository;

import com.example.membermanagerment.model.ThietBi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThietBiRepository extends JpaRepository<ThietBi, Integer> {
    @Query("select max(tb.MaTB) + 1 from ThietBi tb")
    Integer getMax();
    ThietBi findByTenTB(String tenTB);
    @Query("SELECT tb FROM ThietBi tb WHERE " +
    "tb.MaTB LIKE CONCAT('%', :keyword, '%') " +
    "OR tb.tenTB LIKE CONCAT('%', :keyword, '%') " +
    "OR tb.moTaTB LIKE CONCAT('%', :keyword, '%')")
    List<ThietBi> findByKeyword(String keyword);
}

package com.example.membermanagerment.repository;

import com.example.membermanagerment.model.ThongTinSD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThongTinSDRepository extends JpaRepository<ThongTinSD, Integer> {
}

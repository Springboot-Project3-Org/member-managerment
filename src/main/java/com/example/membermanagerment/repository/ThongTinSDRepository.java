package com.example.membermanagerment.repository;

import com.example.membermanagerment.model.ThongTinSD;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ThongTinSDRepository extends JpaRepository<ThongTinSD, Integer> {   
}

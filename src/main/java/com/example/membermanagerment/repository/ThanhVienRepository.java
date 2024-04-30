package com.example.membermanagerment.repository;

import com.example.membermanagerment.model.ThanhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface ThanhVienRepository extends JpaRepository<ThanhVien, BigInteger> {
    ThanhVien findByEmail(String email);
    ThanhVien findBySdt(String sdt);
    ThanhVien findByEmailAndPassword(String email, String password);
    ThanhVien findByMaTVAndPassword(BigInteger maTV, String password);
}

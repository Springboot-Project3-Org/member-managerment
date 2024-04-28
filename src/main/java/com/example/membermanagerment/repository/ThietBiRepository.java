package com.example.membermanagerment.repository;

import com.example.membermanagerment.model.ThietBi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThietBiRepository extends JpaRepository<ThietBi, Integer> {
}

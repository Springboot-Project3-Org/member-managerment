package com.example.membermanagerment.repository;

import com.example.membermanagerment.model.XuLy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XuLyRepository extends JpaRepository<XuLy, Integer> {
}

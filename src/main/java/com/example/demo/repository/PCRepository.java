package com.example.demo.repository;

import com.example.demo.entity.PC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PCRepository extends JpaRepository<PC, Long> {

    // ✅ Correct non-static method
    Optional<PC> findByCollegeEmail(String collegeEmail);

    Optional<PC> findByPhone(String phone);

    boolean existsByCollegeEmail(String collegeEmail);

    boolean existsByPhone(String phone);
}

// Repository

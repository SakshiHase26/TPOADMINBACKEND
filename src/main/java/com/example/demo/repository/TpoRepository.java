package com.example.demo.repository;

import com.example.demo.entity.Tpo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TpoRepository extends JpaRepository<Tpo, Long> {
    Optional<Tpo> findByCollegeEmail(String collegeEmail);
    List<Tpo> findByStatus(String status);
    // @Query("SELECT COUNT(t) FROM Tpo t")
    long countByStatus(String status);
}


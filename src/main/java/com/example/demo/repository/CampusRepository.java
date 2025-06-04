package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Campus;

public interface CampusRepository extends JpaRepository<Campus, Integer> {

    Campus findByCampusName(String campus);}

package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "campus")
public class Campus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer campusId;

    private String campusName;
    private String place;

  
}

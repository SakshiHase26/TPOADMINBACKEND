package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tpo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String idNumber;
    private String designation;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_id")
  private Campus campus;
    private String phone;

    @Column(unique = true)
    private String collegeEmail;

    private String password;
    
  @Column(nullable = false)
  private String status = "pending";
}

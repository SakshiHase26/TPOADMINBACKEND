package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pc")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false, unique = true, name = "college_email")
    private String collegeEmail;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "tpo_id", nullable = false)
    private Tpo tpo;

    // Constructor without ID for creation
    public PC(String name, String phone, String collegeEmail, String password, Tpo tpo) {
        this.name = name;
        this.phone = phone;
        this.collegeEmail = collegeEmail;
        this.password = password;
        this.tpo = tpo;
    }

    // With Lombok's @Data, this is automatically generated:
    public void setTpo(Tpo tpo) {
        this.tpo = tpo;
    }

}
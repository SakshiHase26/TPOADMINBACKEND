package com.example.demo.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TpoLoginResponse {
    private String token;
    private Long tpoId;
    private String message;
    private String status;
}


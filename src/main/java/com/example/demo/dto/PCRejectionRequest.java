package com.example.demo.dto;

import lombok.Data;

@Data
public class PCRejectionRequest {
    private String rejectionReason;
    private String rejectedBy;
}
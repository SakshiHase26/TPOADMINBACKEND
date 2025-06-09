package com.example.demo.dto;


import com.example.demo.entity.PC;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PCRegistrationDTO {
    private Long id;
    private String name;
    private String phone;
    private String collegeEmail;
    private PC.ApprovalStatus approvalStatus;
    private String rejectionReason;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime registrationDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedDate;
    
    private String approvedBy;
    private String tpoName; // From associated TPO
}





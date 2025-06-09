package com.example.demo.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

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
    
    // New fields for approval system
    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;
    
    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;
    
    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();
    
    @Column(name = "approved_date")
    private LocalDateTime approvedDate;
    
    @Column(name = "approved_by")
    private String approvedBy; // TPO admin who approved
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructor without ID for creation
    public PC(String name, String phone, String collegeEmail, String password, Tpo tpo) {
        this.name = name;
        this.phone = phone;
        this.collegeEmail = collegeEmail;
        this.password = password;
        this.tpo = tpo;
        this.approvalStatus = ApprovalStatus.PENDING;
        this.registrationDate = LocalDateTime.now();
    }

    // With Lombok's @Data, this is automatically generated:
    public void setTpo(Tpo tpo) {
        this.tpo = tpo;
    }
    
    public enum ApprovalStatus {
        PENDING, APPROVED, REJECTED
    }
}

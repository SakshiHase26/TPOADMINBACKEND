package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @Column(nullable = false)
    private String jobType;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String jobRole;

    private String jobLocation;

    private String packageDetails;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean performanceBased = false;

    @Enumerated(EnumType.STRING)
    private ModeOfWork modeOfWork = ModeOfWork.OFFICE;

    private String lastDateToApply;

    @Enumerated(EnumType.STRING)
    private JoiningDetails joiningDetails = JoiningDetails.IMMEDIATE;

    private String customJoiningText;

    @Enumerated(EnumType.STRING)
    private JobResponsibilities jobResponsibilities = JobResponsibilities.REFER;

    @Column(columnDefinition = "TEXT")
    private String customResponsibilities;

    private String googleFormLink;
    private String whatsappGroupLink;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoticeStatus status = NoticeStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String rejectionReason;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_pc_id", nullable = false)
    private PC createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_tpo_id")
    private Tpo approvedBy;

    private LocalDateTime approvedAt;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NoticeMapping> noticeMappings;

    // Enums
    public enum ModeOfWork {
        OFFICE, REMOTE, HYBRID
    }

    public enum JoiningDetails {
        IMMEDIATE, CUSTOM
    }

    public enum JobResponsibilities {
        REFER, CUSTOM
    }

    public enum NoticeStatus {
        PENDING, APPROVED, REJECTED, DRAFT
    }

    // Custom constructor for creation
    public Notice(String jobType, String companyName, String jobRole, String jobLocation,
            String packageDetails, Boolean performanceBased, ModeOfWork modeOfWork,
            String lastDateToApply, JoiningDetails joiningDetails, String customJoiningText,
            JobResponsibilities jobResponsibilities, String customResponsibilities,
            String googleFormLink, String whatsappGroupLink, PC createdBy) {
        this.jobType = jobType;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.jobLocation = jobLocation;
        this.packageDetails = packageDetails;
        this.performanceBased = performanceBased;
        this.modeOfWork = modeOfWork;
        this.lastDateToApply = lastDateToApply;
        this.joiningDetails = joiningDetails;
        this.customJoiningText = customJoiningText;
        this.jobResponsibilities = jobResponsibilities;
        this.customResponsibilities = customResponsibilities;
        this.googleFormLink = googleFormLink;
        this.whatsappGroupLink = whatsappGroupLink;
        this.createdBy = createdBy;
        this.status = NoticeStatus.PENDING;
    }
}

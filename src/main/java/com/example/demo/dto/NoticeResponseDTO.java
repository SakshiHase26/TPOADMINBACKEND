package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class NoticeResponseDTO {
    private Long noticeId;
    private String jobType;
    private String companyName;
    private String jobRole;
    private String jobLocation;
    private String packageDetails;
    private Boolean performanceBased;
    private String modeOfWork;
    private String lastDateToApply;
    private String joiningDetails;
    private String customJoiningText;
    private String jobResponsibilities;
    private String customResponsibilities;
    private String googleFormLink;
    private String whatsappGroupLink;
    private String status;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime approvedAt;

    // Creator info
    private String creatorName;
    private String creatorEmail;

    // Approver info
    private String approverName;
    private String approverEmail;

    // Campus and Stream info
    private List<CampusStreamInfo> campusStreams;

    @Data
    public static class CampusStreamInfo {
        private Integer campusId;
        private String campusName;
        private String streamId;
        private String streamName;
    }
}
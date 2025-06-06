package com.example.demo.dto;

import lombok.Data;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class NoticeRequestDTO {
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
    private List<Long> selectedCampusIds;
    private List<String> selectedStreamIds;
    @JsonProperty("createdBy")
    private Long createdBy;

    @JsonProperty("approvedBy")
    private Long approvedBy;
}
package com.example.demo.dto;

public record DashboardSummary(
    // long totalStudents,
    // long placedStudents,
    // long nonPlacedStudents,
    long totalTpos,
    long approvedTpos,
    long pendingTpos
) {}

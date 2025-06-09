package com.example.demo.controller;

import com.example.demo.dto.NoticeRequestDTO;
import com.example.demo.dto.NoticeResponseDTO;
import com.example.demo.entity.Notice;
import com.example.demo.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" })
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * Create a new notice (by PC)
     */
    @PostMapping("/create")
    public ResponseEntity<?> createNotice(@RequestBody NoticeRequestDTO requestDTO) {
        try {
            NoticeResponseDTO response = noticeService.createNotice(requestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * TPO approves a notice
     */
    @PutMapping("/{noticeId}/approve/{tpoId}")
    public ResponseEntity<?> approveNotice(@PathVariable Long noticeId,
            @PathVariable Long tpoId) {
        try {
            NoticeResponseDTO response = noticeService.approveNotice(noticeId, tpoId);
            return ResponseEntity.ok(Map.of(
                "message", "Notice approved successfully",
                "notice", response
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    /**
     * TPO rejects a notice
     */
    @PutMapping("/{noticeId}/reject/{tpoId}")
    public ResponseEntity<?> rejectNotice(@PathVariable Long noticeId,
            @PathVariable Long tpoId,
            @RequestBody Map<String, String> requestBody) {
        try {
            String rejectionReason = requestBody.get("rejectionReason");
            if (rejectionReason == null || rejectionReason.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Rejection reason is required"));
            }
            
            NoticeResponseDTO response = noticeService.rejectNotice(noticeId, tpoId, rejectionReason);
            return ResponseEntity.ok(Map.of(
                "message", "Notice rejected successfully",
                "notice", response
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    /**
     * Get all notices pending TPO approval
     */
    @GetMapping("/pending-approval")
    public ResponseEntity<?> getPendingNoticesForApproval() {
        try {
            List<NoticeResponseDTO> notices = noticeService.getPendingNoticesForApproval();
            return ResponseEntity.ok(Map.of(
                "count", notices.size(),
                "notices", notices
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve pending notices"));
        }
    }

    /**
     * Get all notices approved by a specific TPO
     */
    @GetMapping("/approved-by-tpo/{tpoId}")
    public ResponseEntity<?> getNoticesApprovedByTpo(@PathVariable Long tpoId) {
        try {
            List<NoticeResponseDTO> notices = noticeService.getNoticesApprovedByTpo(tpoId);
            return ResponseEntity.ok(Map.of(
                "count", notices.size(),
                "notices", notices
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve approved notices"));
        }
    }

    /**
     * Get all notices rejected by a specific TPO
     */
    @GetMapping("/rejected-by-tpo/{tpoId}")
    public ResponseEntity<?> getNoticesRejectedByTpo(@PathVariable Long tpoId) {
        try {
            List<NoticeResponseDTO> notices = noticeService.getNoticesRejectedByTpo(tpoId);
            return ResponseEntity.ok(Map.of(
                "count", notices.size(),
                "notices", notices
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve rejected notices"));
        }
    }

    /**
     * Get notices by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getNoticesByStatus(@PathVariable String status) {
        try {
            Notice.NoticeStatus noticeStatus = Notice.NoticeStatus.valueOf(status.toUpperCase());
            List<NoticeResponseDTO> notices = noticeService.getNoticesByStatus(noticeStatus);
            return ResponseEntity.ok(Map.of(
                "status", status.toUpperCase(),
                "count", notices.size(),
                "notices", notices
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid status. Valid values are: PENDING, APPROVED, REJECTED, DRAFT"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve notices by status"));
        }
    }

    /**
     * Get notices created by a specific PC
     */
    @GetMapping("/pc/{pcId}")
    public ResponseEntity<?> getNoticesByPC(@PathVariable Long pcId) {
        try {
            List<NoticeResponseDTO> notices = noticeService.getNoticesByPC(pcId);
            return ResponseEntity.ok(Map.of(
                "pcId", pcId,
                "count", notices.size(),
                "notices", notices
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve notices by PC"));
        }
    }

    /**
     * Get notices handled by a specific TPO
     */
    @GetMapping("/tpo/{tpoId}")
    public ResponseEntity<?> getNoticesByTpo(@PathVariable Long tpoId) {
        try {
            List<NoticeResponseDTO> notices = noticeService.getNoticesByTpo(tpoId);
            return ResponseEntity.ok(Map.of(
                "tpoId", tpoId,
                "count", notices.size(),
                "notices", notices
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve notices by TPO"));
        }
    }

    /**
     * Get pending notices with pagination
     */
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<NoticeResponseDTO> notices = noticeService.getPendingNotices(pageable);
            return ResponseEntity.ok(Map.of(
                "content", notices.getContent(),
                "totalElements", notices.getTotalElements(),
                "totalPages", notices.getTotalPages(),
                "currentPage", notices.getNumber(),
                "size", notices.getSize(),
                "hasNext", notices.hasNext(),
                "hasPrevious", notices.hasPrevious()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve pending notices"));
        }
    }

    /**
     * Get notice by ID
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<?> getNoticeById(@PathVariable Long noticeId) {
        try {
            NoticeResponseDTO notice = noticeService.getNoticeById(noticeId);
            return ResponseEntity.ok(notice);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve notice"));
        }
    }

    /**
     * Delete notice
     */
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<?> deleteNotice(@PathVariable Long noticeId) {
        try {
            noticeService.deleteNotice(noticeId);
            return ResponseEntity.ok(Map.of("message", "Notice deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete notice"));
        }
    }

    /**
     * Get all notices
     */
    @GetMapping
    public ResponseEntity<?> getAllNotices() {
        try {
            List<NoticeResponseDTO> notices = noticeService.getAllNotices();
            return ResponseEntity.ok(Map.of(
                "count", notices.size(),
                "notices", notices
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve all notices"));
        }
    }

    /**
     * Get notice statistics for dashboard
     */
    @GetMapping("/statistics")
    public ResponseEntity<?> getNoticeStatistics() {
        try {
            List<NoticeResponseDTO> pendingNotices = noticeService.getNoticesByStatus(Notice.NoticeStatus.PENDING);
            List<NoticeResponseDTO> approvedNotices = noticeService.getNoticesByStatus(Notice.NoticeStatus.APPROVED);
            List<NoticeResponseDTO> rejectedNotices = noticeService.getNoticesByStatus(Notice.NoticeStatus.REJECTED);
            List<NoticeResponseDTO> allNotices = noticeService.getAllNotices();

            return ResponseEntity.ok(Map.of(
                "total", allNotices.size(),
                "pending", pendingNotices.size(),
                "approved", approvedNotices.size(),
                "rejected", rejectedNotices.size(),
                "pendingPercentage", allNotices.size() > 0 ? (pendingNotices.size() * 100.0 / allNotices.size()) : 0,
                "approvedPercentage", allNotices.size() > 0 ? (approvedNotices.size() * 100.0 / allNotices.size()) : 0,
                "rejectedPercentage", allNotices.size() > 0 ? (rejectedNotices.size() * 100.0 / allNotices.size()) : 0
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve notice statistics"));
        }
    }
}
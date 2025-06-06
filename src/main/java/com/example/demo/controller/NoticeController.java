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

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
@CrossOrigin(origins = { "http://localhost:5173", "http://localhost:3000" })
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping("/create")
    public ResponseEntity<NoticeResponseDTO> createNotice(@RequestBody NoticeRequestDTO requestDTO) {

        try {
            NoticeResponseDTO response = noticeService.createNotice(requestDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{noticeId}/approve/{tpoId}")
    public ResponseEntity<NoticeResponseDTO> approveNotice(@PathVariable Long noticeId,
            @PathVariable Long tpoId) {
        try {
            NoticeResponseDTO response = noticeService.approveNotice(noticeId, tpoId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{noticeId}/reject/{tpoId}")
    public ResponseEntity<NoticeResponseDTO> rejectNotice(@PathVariable Long noticeId,
            @PathVariable Long tpoId,
            @RequestBody String rejectionReason) {
        try {
            NoticeResponseDTO response = noticeService.rejectNotice(noticeId, tpoId, rejectionReason);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesByStatus(@PathVariable String status) {
        try {
            Notice.NoticeStatus noticeStatus = Notice.NoticeStatus.valueOf(status.toUpperCase());
            List<NoticeResponseDTO> notices = noticeService.getNoticesByStatus(noticeStatus);
            return ResponseEntity.ok(notices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/pc/{pcId}")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesByPC(@PathVariable Long pcId) {
        try {
            List<NoticeResponseDTO> notices = noticeService.getNoticesByPC(pcId);
            return ResponseEntity.ok(notices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/tpo/{tpoId}")
    public ResponseEntity<List<NoticeResponseDTO>> getNoticesByTpo(@PathVariable Long tpoId) {
        try {
            List<NoticeResponseDTO> notices = noticeService.getNoticesByTpo(tpoId);
            return ResponseEntity.ok(notices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<Page<NoticeResponseDTO>> getPendingNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<NoticeResponseDTO> notices = noticeService.getPendingNotices(pageable);
            return ResponseEntity.ok(notices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDTO> getNoticeById(@PathVariable Long noticeId) {
        try {
            NoticeResponseDTO notice = noticeService.getNoticeById(noticeId);
            return ResponseEntity.ok(notice);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId) {
        try {
            noticeService.deleteNotice(noticeId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<NoticeResponseDTO>> getAllNotices() {
        try {
            List<NoticeResponseDTO> notices = noticeService.getAllNotices();
            return ResponseEntity.ok(notices);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

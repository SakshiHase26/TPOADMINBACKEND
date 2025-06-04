package com.example.demo.controller;

import com.example.demo.service.SystemSettingsService;
import com.example.demo.dto.TpoLoginRequest;
import com.example.demo.dto.TpoLoginResponse;
import com.example.demo.dto.TpoRegisterRequest;
import com.example.demo.entity.Tpo;
import com.example.demo.entity.SystemSettings;
import com.example.demo.service.TpoService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/tpo")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class TpoController {

    private final TpoService tpoService;
    private final SystemSettingsService settingsService;
   
@PostMapping("/register")
public ResponseEntity<?> register(@RequestBody TpoRegisterRequest req) {
    System.out.println("=== TPO REGISTRATION ATTEMPT ===");
    System.out.println("Request: " + req);
    
    try {
        SystemSettings settings = settingsService.getSettings();
        System.out.println("✅ Settings retrieved: " + settings);
        System.out.println("📋 Allow registrations: " + settings.isAllowRegistrations());
        
        if (!settings.isAllowRegistrations()) {
            System.out.println("❌ Registration blocked by system settings");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Registrations are currently closed");
        }
        
        System.out.println("✅ Registration allowed, proceeding...");
        tpoService.registerTpo(req);
        System.out.println("✅ Registration completed successfully");
        return ResponseEntity.ok("Registration successful. Awaiting approval.");
        
    } catch (Exception e) {
        System.err.println("❌ Registration failed: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Registration failed: " + e.getMessage());
    }
}


    @PostMapping("/login")
    public ResponseEntity<TpoLoginResponse> login(@RequestBody TpoLoginRequest req) {
        return ResponseEntity.ok(tpoService.loginTpo(req));
    }

    @PostMapping("/logout")
public ResponseEntity<String> logout(HttpSession session) {
    session.invalidate();
    return ResponseEntity.ok("Logout successful.");
}
    /** 1) Approve/Reject */
    @PutMapping("/{id}/approve")
    public ResponseEntity<String> approve(
            @PathVariable Long id,
            @RequestParam boolean approved
    ) {
        tpoService.updateApprovalStatus(id, approved);
        return ResponseEntity.ok("TPO " + (approved ? "approved" : "rejected"));
    }

    /** 1) Paginated list */
    @GetMapping("/all")
    public ResponseEntity<Page<Tpo>> all(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        return ResponseEntity.ok(tpoService.getAllTpos(page, size));
    }

    /** 2) Partial update of name & campus */
    @PatchMapping("/{id}")
public ResponseEntity<String> updateDetails(
        @PathVariable Long id,
        @RequestBody UpdateDto body
) {
    tpoService.updateTpoDetails(id, body.getName(), body.getCampusId());
    return ResponseEntity.ok("Details updated");
}


    /** 3) Delete */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        tpoService.deleteTpo(id);
        return ResponseEntity.ok("TPO deleted");
    }

    @GetMapping("/approved")
    public ResponseEntity<List<Tpo>> getApprovedTpos() {
        return ResponseEntity.ok(tpoService.getTposByStatus("approved"));
    }
    @GetMapping("/pending")
    public ResponseEntity<List<Tpo>> getPendingTpos() {
        List<Tpo> pendingTpos = tpoService.getTposByStatus("pending");
        return ResponseEntity.ok(pendingTpos);
    }
    // DTO for PATCH
   public static class UpdateDto {
    private String name;
    private Long campusId; // ✅ Correct: use ID to look up the campus

    public String getName() { return name; }
    public void setName(String n) { name = n; }

    public Long getCampusId() { return campusId; }
    public void setCampusId(Long id) { campusId = id; }
}

}

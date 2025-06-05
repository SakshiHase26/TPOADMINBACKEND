package com.example.demo.controller;


import com.example.demo.dto.*;
import com.example.demo.service.PCApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tpo/pc-approvals")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // Adjust based on your React app port
public class PCApprovalController {
    
    private final PCApprovalService pcApprovalService;
    
    // Get all PC registrations with pagination and filtering
    @GetMapping
    public ResponseEntity<Page<PCRegistrationDTO>> getAllPCRegistrations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "pending") String status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "registrationDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Page<PCRegistrationDTO> registrations = pcApprovalService.getAllPCRegistrations(
            page, size, status, search, sortBy, sortDir);
        
        return ResponseEntity.ok(registrations);
    }
    
    // Get specific PC registration by ID
    @GetMapping("/{id}")
    public ResponseEntity<PCRegistrationDTO> getPCRegistrationById(@PathVariable Long id) {
        return pcApprovalService.getPCRegistrationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Approve PC registration
    @PutMapping("/{id}/approve")
    public ResponseEntity<PCRegistrationDTO> approvePC(
            @PathVariable Long id,
            @RequestBody PCApprovalRequest request) {
        try {
            PCRegistrationDTO approved = pcApprovalService.approvePC(id, request);
            return ResponseEntity.ok(approved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Reject PC registration
    @PutMapping("/{id}/reject")
    public ResponseEntity<PCRegistrationDTO> rejectPC(
            @PathVariable Long id,
            @RequestBody PCRejectionRequest request) {
        try {
            PCRegistrationDTO rejected = pcApprovalService.rejectPC(id, request);
            return ResponseEntity.ok(rejected);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Get PC registration statistics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("pending", pcApprovalService.getPendingCount());
        stats.put("approved", pcApprovalService.getApprovedCount());
        stats.put("rejected", pcApprovalService.getRejectedCount());
        stats.put("total", pcApprovalService.getPendingCount() + 
                          pcApprovalService.getApprovedCount() + 
                          pcApprovalService.getRejectedCount());
        
        return ResponseEntity.ok(stats);
    }
    
    // Get PC registrations for specific TPO
    @GetMapping("/tpo/{tpoId}")
    public ResponseEntity<Page<PCRegistrationDTO>> getPCRegistrationsByTPO(
            @PathVariable Long tpoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "pending") String status,
            @RequestParam(defaultValue = "registrationDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Page<PCRegistrationDTO> registrations = pcApprovalService.getPCRegistrationsByTPO(
            tpoId, page, size, status, sortBy, sortDir);
        
        return ResponseEntity.ok(registrations);
    }

    @PostMapping("/register")
public ResponseEntity<PCRegistrationDTO> registerPC(@RequestBody PCRegistrationDTO request) {
    PCRegistrationDTO registeredPC = pcApprovalService.registerPC(request);
    return ResponseEntity.ok(registeredPC);
}

}
package com.example.demo.service;

import com.example.demo.dto.PCRegistrationDTO;
import com.example.demo.dto.PCApprovalRequest;
import com.example.demo.dto.PCRejectionRequest;
import com.example.demo.entity.PC;
import com.example.demo.entity.Tpo;
import com.example.demo.repository.PCRepository;
import com.example.demo.repository.TpoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PCApprovalService {
    
    private final PCRepository pcRepository;
    private final TpoRepository tpoRepository;
    private final PasswordEncoder passwordEncoder;
    
    public Page<PCRegistrationDTO> getAllPCRegistrations(
            int page, int size, String status, String search, String sortBy, String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        PC.ApprovalStatus approvalStatus = null;
        if (!"all".equalsIgnoreCase(status)) {
            try {
                approvalStatus = PC.ApprovalStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                approvalStatus = PC.ApprovalStatus.PENDING;
            }
        }
        
        Page<PC> pcPage;
        if (search != null && !search.isEmpty()) {
            pcPage = pcRepository.findByStatusAndSearch(approvalStatus, search, pageable);
        } else if (approvalStatus != null) {
            pcPage = pcRepository.findByApprovalStatus(approvalStatus, pageable);
        } else {
            pcPage = pcRepository.findAll(pageable);
        }
        
        return pcPage.map(this::convertToDTO);
    }
    
    public Optional<PCRegistrationDTO> getPCRegistrationById(Long id) {
        return pcRepository.findById(id).map(this::convertToDTO);
    }
    
    public PCRegistrationDTO approvePC(Long id, PCApprovalRequest request) {
        PC pc = pcRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("PC not found"));
        
        if (pc.getApprovalStatus() != PC.ApprovalStatus.PENDING) {
            throw new RuntimeException("PC registration is not in pending status");
        }
        
        pc.setApprovalStatus(PC.ApprovalStatus.APPROVED);
        pc.setApprovedDate(LocalDateTime.now());
        pc.setApprovedBy(request.getApprovedBy());
        pc.setUpdatedAt(LocalDateTime.now());
        
        PC savedPC = pcRepository.save(pc);
        return convertToDTO(savedPC);
    }
    
    public PCRegistrationDTO rejectPC(Long id, PCRejectionRequest request) {
        PC pc = pcRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("PC not found"));
        
        if (pc.getApprovalStatus() != PC.ApprovalStatus.PENDING) {
            throw new RuntimeException("PC registration is not in pending status");
        }
        
        pc.setApprovalStatus(PC.ApprovalStatus.REJECTED);
        pc.setRejectionReason(request.getRejectionReason());
        pc.setUpdatedAt(LocalDateTime.now());
        
        PC savedPC = pcRepository.save(pc);
        return convertToDTO(savedPC);
    }
    
    public long getPendingCount() {
        return pcRepository.countByApprovalStatus(PC.ApprovalStatus.PENDING);
    }
    
    public long getApprovedCount() {
        return pcRepository.countByApprovalStatus(PC.ApprovalStatus.APPROVED);
    }
    
    public long getRejectedCount() {
        return pcRepository.countByApprovalStatus(PC.ApprovalStatus.REJECTED);
    }
    
    public Page<PCRegistrationDTO> getPCRegistrationsByTPO(
            Long tpoId, int page, int size, String status, String sortBy, String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        
        PC.ApprovalStatus approvalStatus = PC.ApprovalStatus.valueOf(status.toUpperCase());
        Page<PC> pcPage = pcRepository.findByTpoIdAndApprovalStatus(tpoId, approvalStatus, pageable);
        
        return pcPage.map(this::convertToDTO);
    }
    
    // New method for PC registration
    public PCRegistrationDTO registerPC(PCRegistrationDTO request) {
        // Check if email already exists
        if (pcRepository.existsByCollegeEmail(request.getCollegeEmail())) {
            throw new RuntimeException("Email already registered");
        }
        
        // Check if phone already exists
        if (pcRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number already registered");
        }
        
        // Find TPO by ID
        Tpo tpo = tpoRepository.findById(request.getId())
            .orElseThrow(() -> new RuntimeException("TPO not found"));
        
        // Create new PC entity
        PC pc = new PC();
        pc.setName(request.getName());
        pc.setPhone(request.getPhone());
        pc.setCollegeEmail(request.getCollegeEmail());
        pc.setPassword(passwordEncoder.encode(request.getPassword()));
        pc.setTpo(tpo);
        pc.setApprovalStatus(PC.ApprovalStatus.PENDING);
        pc.setRegistrationDate(LocalDateTime.now());
        
        PC savedPC = pcRepository.save(pc);
        return convertToDTO(savedPC);
    }
    
    private PCRegistrationDTO convertToDTO(PC pc) {
        PCRegistrationDTO dto = new PCRegistrationDTO();
        dto.setId(pc.getId());
        dto.setName(pc.getName());
        dto.setPhone(pc.getPhone());
        dto.setCollegeEmail(pc.getCollegeEmail());
        dto.setId(pc.getTpo().getId());
        dto.setTpoName(pc.getTpo().getName()); // Assuming Tpo has getName() method
        dto.setApprovedBy(pc.getApprovalStatus().name());
        dto.setRejectionReason(pc.getRejectionReason());
        dto.setRegistrationDate(pc.getRegistrationDate());
        dto.setApprovedDate(pc.getApprovedDate());
        dto.setApprovedBy(pc.getApprovedBy());
        dto.setUpdatedAt(pc.getUpdatedAt());
        return dto;
    }
}
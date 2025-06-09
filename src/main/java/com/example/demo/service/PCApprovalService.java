package com.example.demo.service;



import com.example.demo.dto.*;
import com.example.demo.entity.PC;
import com.example.demo.repository.PCRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PCApprovalService {
    
    private final PCRepository pcRepository;
    
    public Page<PCRegistrationDTO> getAllPCRegistrations(
            int page, 
            int size, 
            String status, 
            String search, 
            String sortBy, 
            String sortDir) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        PC.ApprovalStatus statusEnum = null;
        if (status != null && !status.equals("all")) {
            statusEnum = PC.ApprovalStatus.valueOf(status.toUpperCase());
        }
        
        Page<PC> registrations;
        if (statusEnum != null || (search != null && !search.trim().isEmpty())) {
            registrations = pcRepository.findByStatusAndSearch(statusEnum, search, pageable);
        } else {
            registrations = pcRepository.findAll(pageable);
        }
        
        return registrations.map(this::convertToDTO);
    }
    
    public Optional<PCRegistrationDTO> getPCRegistrationById(Long id) {
        return pcRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public PCRegistrationDTO approvePC(Long id, PCApprovalRequest request) {
        PC pc = pcRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PC registration not found with id: " + id));
        
        if (pc.getApprovalStatus() != PC.ApprovalStatus.PENDING) {
            throw new RuntimeException("Only pending PC registrations can be approved");
        }
        
        pc.setApprovalStatus(PC.ApprovalStatus.APPROVED);
        pc.setApprovedDate(LocalDateTime.now());
        pc.setApprovedBy(request.getApprovedBy());
        pc.setUpdatedAt(LocalDateTime.now());
        pc.setRejectionReason(null); // Clear any previous rejection reason
        
        PC saved = pcRepository.save(pc);
        return convertToDTO(saved);
    }
    
    public PCRegistrationDTO rejectPC(Long id, PCRejectionRequest request) {
        PC pc = pcRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PC registration not found with id: " + id));
        
        if (pc.getApprovalStatus() != PC.ApprovalStatus.PENDING) {
            throw new RuntimeException("Only pending PC registrations can be rejected");
        }
        
        pc.setApprovalStatus(PC.ApprovalStatus.REJECTED);
        pc.setRejectionReason(request.getRejectionReason());
        pc.setApprovedBy(request.getRejectedBy());
        pc.setUpdatedAt(LocalDateTime.now());
        
        PC saved = pcRepository.save(pc);
        return convertToDTO(saved);
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
    
    // Get registrations for specific TPO
    public Page<PCRegistrationDTO> getPCRegistrationsByTPO(
            Long tpoId,
            int page, 
            int size, 
            String status, 
            String sortBy, 
            String sortDir) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        PC.ApprovalStatus statusEnum = null;
        if (status != null && !status.equals("all")) {
            statusEnum = PC.ApprovalStatus.valueOf(status.toUpperCase());
        }
        
        Page<PC> registrations = pcRepository.findByTpoIdAndApprovalStatus(tpoId, statusEnum, pageable);
        return registrations.map(this::convertToDTO);
    }
    
    private PCRegistrationDTO convertToDTO(PC pc) {
        PCRegistrationDTO dto = new PCRegistrationDTO();
        dto.setId(pc.getId());
        dto.setName(pc.getName());
        dto.setPhone(pc.getPhone());
        dto.setCollegeEmail(pc.getCollegeEmail());
        dto.setApprovalStatus(pc.getApprovalStatus());
        dto.setRejectionReason(pc.getRejectionReason());
        dto.setRegistrationDate(pc.getRegistrationDate());
        dto.setApprovedDate(pc.getApprovedDate());
        dto.setApprovedBy(pc.getApprovedBy());
        
        // Set TPO name if available
        if (pc.getTpo() != null) {
            dto.setTpoName(pc.getTpo().getName()); // Assuming TPO has getName() method
        }
        
        return dto;
    }
}
package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.PC;
import com.example.demo.entity.Tpo;
import com.example.demo.repository.PCRepository;
import com.example.demo.repository.TpoRepository;
import com.example.demo.util.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PCAuthService {

    private final PCRepository pcRepo;
    private final TpoRepository tpoRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String registerPC(PCRegisterRequest req) {
        Tpo tpo = tpoRepo.findById(req.getTpoId())
                .orElseThrow(() -> new RuntimeException("TPO not found"));

        PC pc = new PC();
        pc.setName(req.getName());
        pc.setPhone(req.getPhone());
        pc.setCollegeEmail(req.getCollegeEmail());
        pc.setPassword(passwordEncoder.encode(req.getPassword()));
        pc.setTpo(tpo);
        pc.setApprovalStatus(PC.ApprovalStatus.PENDING);
        pc.setUpdatedAt(LocalDateTime.now());

        pcRepo.save(pc);
        return "Placement Coordinator registration submitted successfully. Please wait for TPO approval.";
    }

    public LoginResponse loginPc(LoginRequest request) {
        Optional<PC> optionalPc = pcRepo.findByCollegeEmail(request.getCollegeEmail());

        if (optionalPc.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        PC pc = optionalPc.get();

        if (!passwordEncoder.matches(request.getPassword(), pc.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // Check approval status
        if (pc.getApprovalStatus() == PC.ApprovalStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your account is pending approval from TPO");
        }

        if (pc.getApprovalStatus() == PC.ApprovalStatus.REJECTED) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your account has been rejected by TPO");
        }

        // Only approved users can login
        if (pc.getApprovalStatus() != PC.ApprovalStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access denied");
        }

        String token = jwtUtil.generateToken(pc.getCollegeEmail());

        return new LoginResponse(token, "Login successful");
    }

    // Method for TPO to approve PC
    public String approvePc(Long pcId) {
        PC pc = pcRepo.findById(pcId)
                .orElseThrow(() -> new RuntimeException("PC not found"));

        pc.setApprovalStatus(PC.ApprovalStatus.APPROVED);
        pc.setApprovedDate(LocalDateTime.now());
        pcRepo.save(pc);

        return "Placement Coordinator approved successfully";
    }

    // Method for TPO to reject PC
    public String rejectPc(Long pcId) {
        PC pc = pcRepo.findById(pcId)
                .orElseThrow(() -> new RuntimeException("PC not found"));

        pc.setApprovalStatus(PC.ApprovalStatus.REJECTED);
        pcRepo.save(pc);

        return "Placement Coordinator rejected";
    }

    // Method to get pending PCs for TPO
    public Page<PC> getPendingPCs(Long tpoId) {
        return pcRepo.findByTpoIdAndApprovalStatus(tpoId, PC.ApprovalStatus.PENDING, null);
    }
}
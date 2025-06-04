package com.example.demo.service;

import com.example.demo.dto.TpoLoginRequest;
import com.example.demo.dto.TpoLoginResponse;
import com.example.demo.dto.TpoRegisterRequest;
import com.example.demo.entity.Tpo;
import com.example.demo.entity.Campus;
import com.example.demo.repository.TpoRepository;
import com.example.demo.repository.CampusRepository;
import com.example.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TpoService {

    private final TpoRepository repo;
    private final CampusRepository campusRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Register TPO
    public void registerTpo(TpoRegisterRequest req) {
        if (repo.findByCollegeEmail(req.getCollegeEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        Optional<Campus> campusOpt = campusRepo.findById(req.getCampusId());
        if (!campusOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid campus ID");
        }

        Campus campus = campusOpt.get();
        Tpo t = Tpo.builder()
                .name(req.getName())
                .idNumber(req.getIdNumber())
                .designation(req.getDesignation())
                .campus(campus)
                .phone(req.getPhone())
                .collegeEmail(req.getCollegeEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .status("pending")
                .build();

        repo.save(t);
    }

    // Login TPO
   // Login TPO - Updated with debug logging
public TpoLoginResponse loginTpo(TpoLoginRequest req) {
    Tpo tpo = repo.findByCollegeEmail(req.getCollegeEmail())
        .orElseThrow(() -> new RuntimeException("TPO not found"));

    if (!passwordEncoder.matches(req.getPassword(), tpo.getPassword())) {
        throw new RuntimeException("Invalid password");
    }

    if (!"approved".equalsIgnoreCase(tpo.getStatus())) {
        throw new RuntimeException("TPO not approved yet");
    }

    String token = jwtUtil.generateToken(tpo.getCollegeEmail());

    return new TpoLoginResponse(token, tpo.getId(), "Login successful", tpo.getStatus());
}

    // Approve or Reject TPO
    public void updateApprovalStatus(Long id, boolean approved) {
        Tpo t = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TPO not found"));

        t.setStatus(approved ? "approved" : "rejected");
        repo.save(t);
    }

    // Paginated list of TPOs
    public Page<Tpo> getAllTpos(int page, int size) {
        Pageable pg = PageRequest.of(page, size, Sort.by("id").ascending());
        return repo.findAll(pg);
    }

    // Update name and campus
   public void updateTpoDetails(Long tpoId, String name, Long campusId) {
    Tpo tpo = repo.findById(tpoId)
        .orElseThrow(() -> new RuntimeException("TPO not found"));

    if (name != null) tpo.setName(name);
    if (campusId != null) {
        Campus campus = campusRepo.findById(campusId.intValue()) // 👈 convert to int if necessary
            .orElseThrow(() -> new RuntimeException("Campus not found"));
        tpo.setCampus(campus);
    }

    repo.save(tpo);
}


    // Delete TPO
    public void deleteTpo(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "TPO not found");
        }
        repo.deleteById(id);
    }

    // Get all TPOs by status
    public List<Tpo> getTposByStatus(String status) {
        return repo.findByStatus(status);
    }

    // Count all TPOs
    public long countAll() {
        return repo.count();
    }

    // Count TPOs by status
    public long countByStatus(String status) {
        return repo.countByStatus(status);
    }
}

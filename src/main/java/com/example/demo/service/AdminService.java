package com.example.demo.service;

import com.example.demo.dto.AdminLoginRequest;
import com.example.demo.dto.AdminLoginResponse;
import com.example.demo.dto.AdminRegisterRequest;
import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;
import com.example.demo.util.JwtUtil;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String registerAdmin(AdminRegisterRequest request) {
        if (adminRepository.findByUsername(request.getUsername()).isPresent()) {
            return "Username already registered";
        }

        Admin admin = Admin.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        adminRepository.save(admin);
        return "Admin registration successful.";
    }

    public AdminLoginResponse loginAdmin(AdminLoginRequest request) {
        Optional<Admin> optionalAdmin = adminRepository.findByUsername(request.getUsername());

        if (optionalAdmin.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        Admin admin = optionalAdmin.get();

        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        String token = jwtUtil.generateToken(admin.getUsername());
        return new AdminLoginResponse(token, "Login successful");
    }
    
}

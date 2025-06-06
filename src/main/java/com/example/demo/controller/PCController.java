package com.example.demo.controller;

import com.example.demo.dto.PCRegisterRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
// import com.example.PlacementCell.dto.PCLoginRequest;
import com.example.demo.service.PCAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pc")
@CrossOrigin(origins = "*")
public class PCController {

    private final PCAuthService pcAuthService;

    public PCController(PCAuthService pcAuthService) {
        this.pcAuthService = pcAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody PCRegisterRequest request) {
        try {
            String response = pcAuthService.registerPC(request);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginPc(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(pcAuthService.loginPc(request));
    }

}

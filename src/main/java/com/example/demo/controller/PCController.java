package com.example.demo.controller;

import com.example.demo.dto.PCRegisterRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.service.PCAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public ResponseEntity<?> loginPc(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = pcAuthService.loginPc(request);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body("Login failed: " + e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Login failed: " + e.getMessage());
        }
    }
    
    // Test endpoint to verify controller is working
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("PC Controller is working!");
    }
}
package com.example.demo.controller;

import com.example.demo.entity.SystemSettings;
import com.example.demo.service.SystemSettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SystemSettingsController {

    private final SystemSettingsService service;

    /** GET current settings */
    @GetMapping
    public ResponseEntity<SystemSettings> get() {
        return ResponseEntity.ok(service.getSettings());
    }

    /** PUT new settings (entire object) */
    @PatchMapping
    public ResponseEntity<SystemSettings> update(@RequestBody SystemSettings body) {
        SystemSettings saved = service.updateSettings(body);
        return ResponseEntity.ok(saved);
    }
}

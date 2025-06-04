package com.example.demo.controller;

import com.example.demo.dto.DashboardSummary;
import com.example.demo.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DashboardController {
  private final DashboardService dashboardService;

  @GetMapping("/summary")
  public ResponseEntity<DashboardSummary> summary() {
    DashboardSummary dto = dashboardService.getSummary();
    return ResponseEntity.ok(dto);
  }
}

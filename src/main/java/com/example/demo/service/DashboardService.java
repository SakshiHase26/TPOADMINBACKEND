package com.example.demo.service;

import com.example.demo.dto.DashboardSummary;
// import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TpoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
//   private final StudentRepository studentRepo;
  private final TpoRepository    tpoRepo;

  public DashboardSummary getSummary() {
    // long totalStudents     = studentRepo.count();
    // long placedStudents    = studentRepo.countByPlacedTrue();
    // long nonPlacedStudents = studentRepo.countByPlacedFalse();

    long totalTpos    = tpoRepo.count();
    long approvedTpos = tpoRepo.countByStatus("approved");
    long pendingTpos  = tpoRepo.countByStatus("pending");

    return new DashboardSummary(
    //   totalStudents,
    //   placedStudents,
    //   nonPlacedStudents,
      totalTpos,
      approvedTpos,
      pendingTpos
    );
  }
}

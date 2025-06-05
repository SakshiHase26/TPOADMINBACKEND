package com.example.demo.repository;



import com.example.demo.entity.PC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PCRepository extends JpaRepository<PC, Long> {
    
    Page<PC> findByApprovalStatus(PC.ApprovalStatus status, Pageable pageable);
    
    @Query("SELECT p FROM PC p WHERE " +
           "(:status IS NULL OR p.approvalStatus = :status) AND " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.collegeEmail) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.phone) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<PC> findByStatusAndSearch(
        @Param("status") PC.ApprovalStatus status,
        @Param("search") String search,
        Pageable pageable
    );
    
    long countByApprovalStatus(PC.ApprovalStatus status);
    boolean existsByCollegeEmail(String email);
boolean existsByPhone(String phone);

    
    // Find PCs by TPO (if you want to filter by specific TPO)
    Page<PC> findByTpoIdAndApprovalStatus(Long tpoId, PC.ApprovalStatus status, Pageable pageable);
}

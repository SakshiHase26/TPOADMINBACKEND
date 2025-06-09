package com.example.demo.repository;

import com.example.demo.entity.PC;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PCRepository extends JpaRepository<PC, Long> {
    
    // UPDATED: Count methods for statistics using new approval_status enum
    long countByApprovalStatus(PC.ApprovalStatus status);
    
    // REMOVED: These old methods that caused the error
    // long countByApprovedTrue();
    // long countByApprovedFalseAndRejectionReasonIsNull();
    // long countByApprovedFalseAndRejectionReasonIsNotNull();
    
    // NEW: Count methods using the new enum system
    default long countApproved() {
        return countByApprovalStatus(PC.ApprovalStatus.APPROVED);
    }
    
    default long countPending() {
        return countByApprovalStatus(PC.ApprovalStatus.PENDING);
    }
    
    default long countRejected() {
        return countByApprovalStatus(PC.ApprovalStatus.REJECTED);
    }
    
    // UPDATED: TPO specific queries using new approval_status
    Page<PC> findByTpoId(Long tpoId, Pageable pageable);
    Page<PC> findByTpoIdAndApprovalStatus(Long tpoId, PC.ApprovalStatus status, Pageable pageable);
    
    // REMOVED: Old boolean-based queries
    // Page<PC> findByTpoIdAndApprovedTrue(Long tpoId, Pageable pageable);
    // Page<PC> findByTpoIdAndApprovedFalseAndRejectionReasonIsNull(Long tpoId, Pageable pageable);
    // Page<PC> findByTpoIdAndApprovedFalseAndRejectionReasonIsNotNull(Long tpoId, Pageable pageable);
    
    // NEW: More specific TPO queries
    default Page<PC> findApprovedByTpoId(Long tpoId, Pageable pageable) {
        return findByTpoIdAndApprovalStatus(tpoId, PC.ApprovalStatus.APPROVED, pageable);
    }
    
    default Page<PC> findPendingByTpoId(Long tpoId, Pageable pageable) {
        return findByTpoIdAndApprovalStatus(tpoId, PC.ApprovalStatus.PENDING, pageable);
    }
    
    default Page<PC> findRejectedByTpoId(Long tpoId, Pageable pageable) {
        return findByTpoIdAndApprovalStatus(tpoId, PC.ApprovalStatus.REJECTED, pageable);
    }
    
    // KEPT: This method is fine as is
    Page<PC> findByApprovalStatus(PC.ApprovalStatus status, Pageable pageable);
    
    // KEPT: Custom search query is good
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
    
    // KEPT: These basic methods are fine
    Optional<PC> findByCollegeEmail(String collegeEmail);
    boolean existsByPhone(String phone);
    boolean existsByCollegeEmail(String collegeEmail);
}
package com.example.demo.repository;

import com.example.demo.entity.Notice;
import com.example.demo.entity.PC;
import com.example.demo.entity.Tpo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findByCreatedBy(PC createdBy);

    List<Notice> findByStatus(Notice.NoticeStatus status);

    @Query("SELECT n FROM Notice n JOIN n.noticeMappings nm WHERE nm.tpo = :tpo")
    List<Notice> findByTpo(@Param("tpo") Tpo tpo);

    @Query("SELECT n FROM Notice n JOIN n.noticeMappings nm WHERE nm.campus.campusId = :campusId")
    List<Notice> findByCampusId(@Param("campusId") Integer campusId);

    @Query("SELECT n FROM Notice n JOIN n.noticeMappings nm WHERE nm.stream.streamId = :streamId")
    List<Notice> findByStreamId(@Param("streamId") String streamId);

    List<Notice> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    Page<Notice> findByStatusOrderByCreatedAtDesc(Notice.NoticeStatus status, Pageable pageable);

    @Query("SELECT COUNT(n) FROM Notice n WHERE n.createdBy = :pc AND n.createdAt >= :startDate")
    Long countNoticesByPcSince(@Param("pc") PC pc, @Param("startDate") LocalDateTime startDate);
}
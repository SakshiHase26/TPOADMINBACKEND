package com.example.demo.repository;

import com.example.demo.entity.NoticeMapping;
import com.example.demo.entity.Notice;
import com.example.demo.entity.Campus;
import com.example.demo.entity.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeMappingRepository extends JpaRepository<NoticeMapping, Long> {

    List<NoticeMapping> findByNotice(Notice notice);

    List<NoticeMapping> findByCampus(Campus campus);

    List<NoticeMapping> findByStream(Stream stream);

    @Query("SELECT nm FROM NoticeMapping nm WHERE nm.notice.noticeId = :noticeId")
    List<NoticeMapping> findByNoticeId(@Param("noticeId") Long noticeId);

    @Query("SELECT nm FROM NoticeMapping nm WHERE nm.campus.campusId IN :campusIds AND nm.stream.streamId IN :streamIds")
    List<NoticeMapping> findByCampusIdsAndStreamIds(@Param("campusIds") List<Integer> campusIds,
            @Param("streamIds") List<String> streamIds);

    void deleteByNotice(Notice notice);
}
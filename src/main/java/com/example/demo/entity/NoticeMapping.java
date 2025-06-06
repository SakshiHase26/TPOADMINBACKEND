package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notice_mapping")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeMapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mappingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id", nullable = false)
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pc_id", nullable = false)
    private PC placementCoordinator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tpo_id", nullable = false)
    private Tpo tpo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id", nullable = false)
    private Campus campus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stream_id", nullable = false)
    private Stream stream;

    // Constructor for easy creation
    public NoticeMapping(Notice notice, PC placementCoordinator, Tpo tpo, Campus campus, Stream stream) {
        this.notice = notice;
        this.placementCoordinator = placementCoordinator;
        this.tpo = tpo;
        this.campus = campus;
        this.stream = stream;
    }
}
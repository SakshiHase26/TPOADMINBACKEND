package com.example.demo.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "stream")
public class Stream {

    @Id
    @Column(name = "stream_id", length = 20)
    private String streamId;

    @Column(name = "stream_name", nullable = false, length = 150)
    private String streamName;

    @ManyToOne
    @JoinColumn(name = "campus_id")
    private Campus campus;

    @Column(name = "year", length = 50)
    private String year;

    // Getters and Setters

    public String getStreamId() {
        return streamId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public String getStreamName() {
        return streamName;
    }

    public void setStreamName(String streamName) {
        this.streamName = streamName;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}

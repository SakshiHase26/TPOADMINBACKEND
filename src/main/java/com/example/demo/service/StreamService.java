package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Campus;
import com.example.demo.entity.Stream;
import com.example.demo.repository.StreamRepository;
import com.example.demo.repository.CampusRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class StreamService {

    private final StreamRepository streamRepository;
    private final CampusRepository campusRepository;

    public StreamService(StreamRepository streamRepository, CampusRepository campusRepository) {
        this.streamRepository = streamRepository;
        this.campusRepository = campusRepository;
    }

    public List<Stream> getAllStreams() {
        return streamRepository.findAll();
    }

    public Stream addStream(Stream stream) {
        Integer campusId = stream.getCampus().getCampusId(); // only ID comes from frontend
        Campus campus = campusRepository.findById(campusId)
                .orElseThrow(() -> new NoSuchElementException("Campus not found with ID: " + campusId));
        
        stream.setCampus(campus); // set full Campus object before saving
        return streamRepository.save(stream);
    }

    public void deleteStream(String streamId) {
        streamRepository.deleteById(streamId);
    }

    public Stream updateStream(String id, Stream streamDetails) {
        Stream existingStream = streamRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Stream not found with id " + id));

        existingStream.setStreamName(streamDetails.getStreamName());
        existingStream.setYear(streamDetails.getYear());

        // Fetch and update campus if needed
        Integer campusId = streamDetails.getCampus().getCampusId();
        Campus campus = campusRepository.findById(campusId)
                .orElseThrow(() -> new NoSuchElementException("Campus not found with ID: " + campusId));
        existingStream.setCampus(campus);

        return streamRepository.save(existingStream);
    }
}

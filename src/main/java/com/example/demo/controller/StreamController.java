package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Stream;
import com.example.demo.service.StreamService;

import java.util.List;

@RestController
@RequestMapping("/api/stream")
@CrossOrigin(origins = "http://localhost:3000")
public class StreamController {

    private final StreamService streamService;

    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    @GetMapping("/all")
    public List<Stream> getAllStreams() {
        return streamService.getAllStreams();
    }

    @PostMapping("/add")
    public Stream addStream(@RequestBody Stream stream) {
        return streamService.addStream(stream);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteStream(@PathVariable String id) {
        streamService.deleteStream(id);
    }
  
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateStream(@PathVariable String id, @RequestBody Stream streamDetails) {
        Stream updatedStream = streamService.updateStream(id, streamDetails);
        return ResponseEntity.ok(updatedStream);
    }
}

package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Campus;
import com.example.demo.service.CampusService;

@RestController
@RequestMapping("/api/admin/campus")
public class CampusController {
    @Autowired
    private CampusService service;

    @GetMapping("/all")
    public List<Campus> getAll() {
        return service.getAll();
    }

    @PostMapping("/add")
    public Campus add(@RequestBody Campus campus) {
        return service.addCampus(campus);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.deleteCampus(id);
        return ResponseEntity.ok("Deleted");
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Campus campusDetails) {
        Campus updatedCampus = service.updateCampus(id, campusDetails);
        return ResponseEntity.ok(updatedCampus);
    }
}

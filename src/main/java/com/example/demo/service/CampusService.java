package com.example.demo.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Campus;
import com.example.demo.repository.CampusRepository;

@Service
public class CampusService {
    @Autowired
    private CampusRepository repo;

    public List<Campus> getAll() {
        return repo.findAll();
    }

    public Campus addCampus(Campus campus) {
        return repo.save(campus);
    }

    public void deleteCampus(Integer id) {
        repo.deleteById(id);
    }


       public Campus updateCampus(Integer id, Campus campusDetails) {
        Campus existingCampus = repo.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Campus not found with id " + id));

        existingCampus.setCampusName(campusDetails.getCampusName());
        existingCampus.setPlace(campusDetails.getPlace());
       

        return repo.save(existingCampus);
    }
}


package com.example.demo.controller;

import com.example.demo.model.Lecturer;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Validator;
import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.LecturerRepository;

@RestController
@RequestMapping("/lecturer")
@RateLimiter(name = "rateLimit")
public class LecturerController {
    @Autowired
    LecturerRepository lecturerRepository;


    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Lecturer lecturer) {
        if (lecturer.getId() != null && lecturerRepository.existsById(lecturer.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("A lecturer with this ID already exists");
        }
         return ResponseEntity.ok(lecturerRepository.save(lecturer));
    }

    @GetMapping("/{lecturerId}")
    public ResponseEntity<Lecturer> getLecturerById(@PathVariable Long lecturerId) {
        return lecturerRepository.findById(lecturerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}

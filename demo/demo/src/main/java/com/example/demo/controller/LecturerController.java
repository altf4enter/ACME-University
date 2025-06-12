package com.example.demo.controller;

import com.example.demo.dto.LecturerDTO;
import com.example.demo.model.Lecturer;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
         return ResponseEntity.ok(LecturerDTO.toLecturerDTO(lecturerRepository.save(lecturer)));
    }

    @GetMapping("/{lecturerId}")
    public ResponseEntity<LecturerDTO> getLecturerById(@PathVariable Long lecturerId) {
        return lecturerRepository.findById(lecturerId)
                .map(res -> {
                    var dto = LecturerDTO.toLecturerDTO(res);
                    return ResponseEntity.ok(dto);
                })

                .orElse(ResponseEntity.notFound().build());
    }

}

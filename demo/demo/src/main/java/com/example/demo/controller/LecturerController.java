package com.example.demo.controller;

import com.example.demo.model.Lecturer;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Validator;
import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.LecturerRepository;

@RestController
@RequestMapping("/lecturer")
@RateLimiter(name = "rateLimit", fallbackMethod = "rateLimitFallback")
public class LecturerController {
    @Autowired
    LecturerRepository lecturerRepository;

    @Autowired
    private Validator validator;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Lecturer lecturer) {
        if(lecturerRepository.existsById(lecturer.getId())){
            //throw exception
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Item already exists");
        }
        if(!validator.validate(lecturer).isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Item already exists");
        }
         return ResponseEntity.ok(lecturerRepository.save(lecturer));
    }

    @GetMapping("/{lecturerId}")
    public ResponseEntity<Lecturer> getLecturerById(@PathVariable Long lecturerId) {
        return lecturerRepository.findById(lecturerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLecturerById(@PathVariable Long id) {
        lecturerRepository.deleteById(id);

        return ResponseEntity.ok("Lecturer has been deleted");
    }
}

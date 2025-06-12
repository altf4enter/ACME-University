package com.example.demo.controller;

import com.example.demo.model.Student;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.LecturerRepository;
import com.example.demo.repository.StudentRepository;

@RestController
@RequestMapping("/student")
@RateLimiter(name = "rateLimit", fallbackMethod = "rateLimitFallback")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LecturerRepository lecturerRepository;
    @Autowired
    private Validator validator;

    @PostMapping("/add/{lecturerId}")
    public ResponseEntity<?> create(@RequestBody Student student, @PathVariable Long lecturerId) {

        var lecturer = lecturerRepository.findById(lecturerId);
        var lecturers = student.getLecturerIds();
        if(lecturer.isEmpty(
        )){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Lecturer doesnt exist");
        }

        lecturer.ifPresent(lect -> {
                    lecturers.add(lect.getId());
                }
        );
        student.setLecturerIds(lecturers);

        if(!studentRepository.findById(student.getId()).isEmpty()){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Item already exists");
        };
        if(!validator.validate(student).isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Item already exists");
        }

        return ResponseEntity.ok(studentRepository.save(student));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long studentId) {
        return studentRepository.findById(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudentById(@PathVariable Long id) {
        studentRepository.deleteById(id);

        return ResponseEntity.ok("Student has been deleted");
    }
}

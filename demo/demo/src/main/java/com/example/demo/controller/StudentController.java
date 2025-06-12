package com.example.demo.controller;

import com.example.demo.model.Lecturer;
import com.example.demo.model.Student;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.LecturerRepository;
import com.example.demo.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/student")
@RateLimiter(name = "rateLimit")
public class StudentController {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private LecturerRepository lecturerRepository;


    @PostMapping("/add/{lecturerId}")
    public ResponseEntity<?> create(@Valid @RequestBody Student student, @PathVariable Long lecturerId) {

        var lecturer = lecturerRepository.findById(lecturerId);
        if(lecturer.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Lecturer doesnt exist");
        }
        if(student.getLecturers() != null && !student.getLecturers().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Student cannot have multiple lecturers assigned to on creation.");
        }

        student.setLecturers(List.of(lecturer.get()));

        if (student.getId() != null && studentRepository.existsById(student.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("A student with this ID already exists");
        }

        return ResponseEntity.ok(studentRepository.save(student));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long studentId) {
        return studentRepository.findById(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}

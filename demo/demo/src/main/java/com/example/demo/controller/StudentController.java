package com.example.demo.controller;

import com.example.demo.model.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.repository.LecturerRepository;
import com.example.demo.repository.StudentRepository;

@RestController
@RequestMapping("/student")
public class StudentController {
    private StudentRepository studentRepository;
    private LecturerRepository lecturerRepository;

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
                    lecturers.add(lect.getId().toString());
                }
        );
        student.setLecturerIds(lecturers);

        return ResponseEntity.ok(studentRepository.save(student));
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long studentId) {
        return studentRepository.findById(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

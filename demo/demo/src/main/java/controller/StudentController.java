package controller;

import model.Lecturer;
import model.Person;
import model.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.LecturerRepository;
import repository.StudentRepository;

@RestController
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
                    lecturers.add(lect.getLecturerId().toString());
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

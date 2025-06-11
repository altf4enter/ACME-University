package controller;

import model.Lecturer;
import model.Person;
import model.Student;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.LecturerRepository;
import repository.StudentRepository;

@RestController
public class StudentController {
    private StudentRepository studentRepository;
    private LecturerRepository lecturerRepository;

    @PostMapping("/add/{lecturerId}")
    public Student create(@RequestBody Student student, @PathVariable Long lecturerId) {

        var lecturer = lecturerRepository.findById(lecturerId);
        var lecturers = student.getLecturerIds();
        if(lecturer.isEmpty(
        )){
        }

        lecturer.ifPresent(lect -> {
                    lecturers.add(lect.getLecturerId().toString());
                }
        );

        lecturers.add(lecturer);
        student.setLecturerIds();
        studentRepository.save(student);

        return studentRepository.save(student);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long studentId) {
        return studentRepository.findById(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

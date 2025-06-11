package controller;

import model.Lecturer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.LecturerRepository;

@RestController
public class LecturerController {
    @Autowired
    LecturerRepository lecturerRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Lecturer lecturer) {
        if(lecturerRepository.existsById(lecturer.getLecturerId())){
            //throw exception
            return ResponseEntity.status(HttpStatus.CONFLICT)
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
}

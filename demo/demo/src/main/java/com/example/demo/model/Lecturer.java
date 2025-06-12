package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Lecturer extends  Person {

    public Lecturer(String name, String surname, List<Student> students){
        super(name,surname);
       this.students = students;
    }

    @ManyToMany
    @JoinTable(
            name = "lecturer_student",
            joinColumns = @JoinColumn(name = "lecturer_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private List<Student> students;
}

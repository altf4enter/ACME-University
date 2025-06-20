package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Student extends  Person {

    public Student(String name, String surname, List<Lecturer> lecturers){
        super(name,surname);
        this.lecturers = lecturers;
    }

    @ManyToMany(mappedBy = "students")
    private List<Lecturer> lecturers;
}

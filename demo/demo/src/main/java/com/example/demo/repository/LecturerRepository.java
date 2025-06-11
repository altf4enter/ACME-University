package com.example.demo.repository;

import com.example.demo.model.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
    List<Lecturer> findByNameAndSurname(String name, String surname);
}

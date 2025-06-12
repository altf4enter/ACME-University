package com.example.demo.dto;


import com.example.demo.model.Student;
import lombok.Data;

@Data
public class StudentSummaryDTO {
    private Long id;
    private String name;
    private String surname;

    public StudentSummaryDTO toStudentSummaryDTO(Student student) {
        StudentSummaryDTO summary = new StudentSummaryDTO();
        summary.setId(student.getId());
        summary.setName(student.getName());
        summary.setSurname(student.getSurname());
        return summary;
    }
}

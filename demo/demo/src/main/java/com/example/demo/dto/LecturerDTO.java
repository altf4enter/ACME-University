package com.example.demo.dto;

import com.example.demo.model.Lecturer;
import com.example.demo.model.Student;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LecturerDTO {
    public static  LecturerDTO toLecturerDTO(Lecturer lecturer) {
        LecturerDTO dto = new LecturerDTO();
        dto.setId(lecturer.getId());
        dto.setName(lecturer.getName());
        dto.setSurname(lecturer.getSurname());
        dto.setStudents(lecturer.getStudents() != null?
                lecturer.getStudents().stream()
                        .map(LecturerDTO::toStudentSummaryDTO)
                        .collect(Collectors.toList()): List.of()
        );
        return dto;
    }

    public static StudentSummaryDTO toStudentSummaryDTO(Student student) {
        StudentSummaryDTO summary = new StudentSummaryDTO();
        summary.setId(student.getId());
        summary.setName(student.getName());
        summary.setSurname(student.getSurname());
        return summary;
    }

    private Long id;
    private String name;
    private String surname;
    private List<StudentSummaryDTO> students;  // shallow ref to avoid nesting
}

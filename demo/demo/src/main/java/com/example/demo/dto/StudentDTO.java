// Full StudentDTO with lecturers as summaries
package com.example.demo.dto;

import com.example.demo.model.Lecturer;
import com.example.demo.model.Student;
import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class StudentDTO {
    private Long id;
    private String name;
    private String surname;
    private List<LecturerSummaryDTO> lecturers;

    public static  StudentDTO toStudentDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setSurname(student.getSurname());
        dto.setLecturers( student.getLecturers() != null ?
                student.getLecturers().stream()
                        .map(StudentDTO::toLecturerSummaryDTO)
                        .collect(Collectors.toList()): List.of()
        );
        return dto;
    }

    public static LecturerSummaryDTO toLecturerSummaryDTO(Lecturer lecturer) {
        LecturerSummaryDTO summary = new LecturerSummaryDTO();
        summary.setId(lecturer.getId());
        summary.setName(lecturer.getName());
        summary.setSurname(lecturer.getSurname());
        return summary;
    }
}

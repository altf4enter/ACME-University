package com.example.demo;

import com.example.demo.dto.LecturerDTO;
import com.example.demo.dto.StudentSummaryDTO;
import com.example.demo.model.Lecturer;
import com.example.demo.model.Student;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StudentTest {

    private final String baseStudentUrl = "/student";
    private final String baseLecturerUrl = "/lecturer";

    private Lecturer lecturer;
    private Student student;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        lecturer = new Lecturer("Lecturer", "McSurname", null);
        student = new Student("Student", "McSurname", null);
    }

    @Test
    void testCreateStudent() throws Exception {
        Lecturer createdLecturer = createLecturer(lecturer);

        Student createdStudent = createStudent(student, createdLecturer.getId());

        createdStudent.setLecturers(List.of());
        Integer createSameStatus = createStudentStatus(createdStudent, createdLecturer.getId());
        assertEquals(HttpStatus.CONFLICT.value(), createSameStatus);

        createSameStatus = createStudentStatus(createdStudent, -1L);
        assertEquals(HttpStatus.NOT_FOUND.value(), createSameStatus);

    }

    @Test
    void testRetrieveStudent() throws Exception {
        Lecturer createdLecturer = createLecturer(lecturer);

        Student createdStudent = createStudent(student, createdLecturer.getId());

        Student retrievedStudent = getStudentById(createdStudent.getId());
        assertEquals(createdStudent.getName(), retrievedStudent.getName());
        assertEquals(createdStudent.getSurname(), retrievedStudent.getSurname());
        assertEquals(createdStudent.getId(), retrievedStudent.getId());
        assertEquals(1, retrievedStudent.getLecturers().size());
        assertEquals(createdLecturer.getId(), retrievedStudent.getLecturers().get(0).getId());

        LecturerDTO lecturerDTO = getLecturerById(createdLecturer.getId());
        assertEquals(1, lecturerDTO.getStudents().size());
        assertTrue(lecturerDTO.getStudents().contains(StudentSummaryDTO.toStudentSummaryDTO(retrievedStudent)));


        Integer status = getStudentStatus(-1L);
        assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    private Lecturer createLecturer(Lecturer lecturer) throws Exception {
        MvcResult result = mockMvc.perform(post(baseLecturerUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lecturer)))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        return objectMapper.readValue(json, Lecturer.class);
    }

    public LecturerDTO getLecturerById(Long id) throws Exception {
        MvcResult result = mockMvc.perform(get(baseLecturerUrl + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        return objectMapper.readValue(json, LecturerDTO.class);
    }

    private Student createStudent(Student student, Long lecturerId) throws Exception {
        MvcResult result = mockMvc.perform(post(baseStudentUrl + "/add/" + lecturerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        return objectMapper.readValue(json, Student.class);
    }

    public int createStudentStatus(Student student, Long lecturerId) throws Exception {
        return mockMvc.perform(post(baseStudentUrl + "/add/" + lecturerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andReturn().getResponse().getStatus();
    }

    private Student getStudentById(Long studentId) throws Exception {
        MvcResult result = mockMvc.perform(get(baseStudentUrl + "/" + studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        return objectMapper.readValue(json, Student.class);
    }

    public int getStudentStatus(Long studentId) throws Exception {
        return  mockMvc.perform(get(baseStudentUrl + "/" + studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getStatus();
    }
}

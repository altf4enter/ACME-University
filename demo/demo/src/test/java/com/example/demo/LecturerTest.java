package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.model.Lecturer;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LecturerTest {

    private String baseUrl;
    private Lecturer lecturer;
    private Lecturer invalidLecturer;

    @BeforeEach
    void setup() {
        baseUrl = "/lecturer";
        lecturer = new Lecturer("Lecturer","McLecturer", List.of());
        invalidLecturer = new Lecturer();
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateLecturer() throws Exception {
        Lecturer created = createLecturer(lecturer);

        int conflictStatus = createLecturerStatus(created);
        assertEquals(HttpStatus.CONFLICT.value(), conflictStatus);
    }

    @Test
    void testRetrieveLecturer() throws Exception {
        Lecturer created = createLecturer(lecturer);
        Lecturer retrieved = getLecturerById(created.getId());

        assertEquals(created.getId(), retrieved.getId());
        assertEquals(lecturer.getName(), retrieved.getName());
        assertEquals(lecturer.getSurname(), retrieved.getSurname());

        Integer status = getLecturerStatus(-1L);
        assertEquals(HttpStatus.NOT_FOUND.value(), status);
    }

    @Test
    void testFieldValidations() throws Exception {
        invalidLecturer.setName("");
        shouldNotCreate(invalidLecturer);

        invalidLecturer.setSurname("");
        shouldNotCreate(invalidLecturer);

        invalidLecturer.setSurname("%");
        shouldNotCreate(invalidLecturer);

        invalidLecturer.setSurname("abc");
        invalidLecturer.setName("%");
        shouldNotCreate(invalidLecturer);
    }

    private void shouldNotCreate(Lecturer lecturer) throws Exception {
        int status = createLecturerStatus(lecturer);
        assertEquals(HttpStatus.BAD_REQUEST.value(), status);
    }

    public Lecturer createLecturer(Lecturer lecturer) throws Exception {
        MvcResult result = mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lecturer)))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        return objectMapper.readValue(json, Lecturer.class);
    }

    public int createLecturerStatus(Lecturer lecturer) throws Exception {
        return mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lecturer)))
                .andReturn().getResponse().getStatus();
    }

    public Lecturer getLecturerById(Long id) throws Exception {
        MvcResult result = mockMvc.perform(get(baseUrl + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        return objectMapper.readValue(json, Lecturer.class);
    }

    public int getLecturerStatus(Long lecturerId) throws Exception {
        return  mockMvc.perform(get(baseUrl + "/" + lecturerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getStatus();
    }
}

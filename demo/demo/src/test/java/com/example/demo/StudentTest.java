package com.example.demo;

import com.example.demo.model.Lecturer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class StudentTest {

    private String baseUrl;
    private String baseLecturerUrl;
    Student student;
    Lecturer lecturer;


    StudentTest(){
        baseUrl =  "http://localhost:8080/student";
        baseLecturerUrl =  "http://localhost:8080/lecturer";

        lecturer = new Lecturer( );
        lecturer.setId(2L);
        lecturer.setName("Lecturer");
        lecturer.setSurname("McSurname");

        student = new Student( );
        student.setName( "Student" );
        student.setSurname("McSurname");
        student.setId(1L);
        student.setLecturers(List.of(lecturer));
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateStudent() throws Exception {

        mockMvc.perform(delete(baseUrl + "/" + student.getId() )
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(post(baseLecturerUrl )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lecturer)));


        mockMvc.perform(post(baseUrl + "/add/" +student.getLecturers().get(0))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());

        //try again and expect a conflict message
        mockMvc.perform(post(baseUrl+ "/add/" +student.getLecturers().get(0))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isConflict());
    }

    @Test
    void testRetrieveStudent() throws Exception {
        mockMvc.perform(post(baseLecturerUrl )
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(lecturer)));

        mockMvc.perform(post(baseUrl + "/add/"+lecturer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)));

        MvcResult res = mockMvc.perform(get(baseUrl+"/" + student.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String resJson = res.getResponse().getContentAsString();
        Student responseStudent = objectMapper.readValue(resJson, Student.class);

    }
}

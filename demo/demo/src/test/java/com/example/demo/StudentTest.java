package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.model.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class StudentTest {

    @LocalServerPort
    private int port;
    private String baseUrl;
    Student student;

    StudentTest(){
        baseUrl =  "http://localhost:8080/student";
        student = new Student( );
        student.setName( "Student" );
        student.setSurname("McSurname");
        student.setId(1L);
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



        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk());

        //try again and expect a conflict message
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isConflict());
    }

    @Test
    void testRetrieveStudent() throws Exception {

        MvcResult result = mockMvc.perform(post(baseUrl + "/add/"+1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        Student student = objectMapper.readValue(responseJson, Student.class);

        MvcResult res = mockMvc.perform(get(baseUrl + student.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String resJson = res.getResponse().getContentAsString();
        Student responseStudent = objectMapper.readValue(resJson, Student.class);

    }
}

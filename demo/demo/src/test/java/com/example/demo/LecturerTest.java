package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.demo.model.Lecturer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LecturerTest {

    private String baseUrl;
    private Lecturer lecturer;
    private Lecturer invalidLecturer;

    LecturerTest(){
        baseUrl =  "http://localhost:8080/lecturer";
        lecturer = new Lecturer();
        lecturer.setName("Lecturer");
        lecturer.setSurname("McLecturer");
        lecturer.setId(1L);

        invalidLecturer.setId(2L);
    }


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateLecturer() throws Exception {

        mockMvc.perform(delete(baseUrl + "/" + lecturer.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        mockMvc.perform(post(baseUrl )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lecturer)))
                .andExpect(status().isOk());

        //try again and expect a conflict message
        mockMvc.perform(post(baseUrl )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lecturer)))
                .andExpect(status().isConflict());
    }

    @Test
    void testRetrieveLecturer() throws Exception {
        String lecturerId;
        MvcResult result = mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lecturer)))
                .andExpect(status().isOk())
                .andReturn();

        // Convert JSON response to Lecturer object
        String responseJson = result.getResponse().getContentAsString();
        Lecturer lecturer = objectMapper.readValue(responseJson, Lecturer.class);

        MvcResult res = mockMvc.perform(get(baseUrl +"/"+  lecturer.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String resJson = res.getResponse().getContentAsString();
        Lecturer responseLecturer = objectMapper.readValue(resJson, Lecturer.class);


    }

    @Test
    void testFieldValidations() throws Exception {
        invalidLecturer.setName(null);
        invalidLecturer.setSurname(null);

        mockMvc.perform(delete(baseUrl + "/"+invalidLecturer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLecturer)))
                .andExpect(status().isOk());

        invalidLecturer.setName("");
        shouldNotCreateLecturer(invalidLecturer);
        invalidLecturer.setSurname("");
        shouldNotCreateLecturer(invalidLecturer);
        invalidLecturer.setSurname("%");
        shouldNotCreateLecturer(invalidLecturer);
        invalidLecturer.setSurname("abc");
        invalidLecturer.setName("%");
        shouldNotCreateLecturer(invalidLecturer);

    }

    public void shouldNotCreateLecturer(Lecturer lecturer) throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lecturer)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }
}

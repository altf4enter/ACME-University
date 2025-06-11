package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonTest {

    private int port;
    private String baseUrl;

    PersonTest(){
        baseUrl =  "http://localhost:"+port+"/lecturer";
    }


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFieldValidations() throws Exception {

        mockMvc.perform(delete(baseUrl + "/Helen/Schloh")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        String lecturerJson = "{\"name\":\"Helen\",\"surname\":\"Schloh\"}";

        mockMvc.perform(post(baseUrl + "/lecturer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lecturerJson))
                .andExpect(status().isOk());

        //try again and expect a conflict message
        mockMvc.perform(post(baseUrl + "/lecturer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(lecturerJson))
                .andExpect(status().isConflict());
    }
}

package com.sumerge.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumerge.task.model.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthorControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void addAuthor_emailNotExists_shouldReturnAuthorDTO() throws Exception {
        Author author = new Author();
        author.setName("Test Author");
        author.setEmail("author@example.com");
        author.setPassword(passwordEncoder.encode("password"));
        author.setCourses(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.post("/authors/add")
                        .header("x-validation-report", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(author.getName()))
                .andExpect(jsonPath("$.email").value(author.getEmail()));
    }

    @Test
    public void addAuthor_emailExists_shouldThrowEmailAlreadyExistsException() throws Exception {
        Author author = new Author();
        author.setName("Test Author");
        author.setEmail("alice@example.com");
        author.setPassword(passwordEncoder.encode("password"));
        author.setCourses(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.post("/authors/add")
                        .header("x-validation-report", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isConflict())
                .andExpect(content().string("The email 'alice@example.com' is already in use. Please choose a different email."));
    }

    @Test
    public void getAuthorByEmail_authorFound_shouldReturnAuthorDTO() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/authors/getByEmail")
                        .header("x-validation-report", "true")
                        .param("email", "alice@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Smith"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    public void getAuthorByEmail_authorNotFound_shouldThrowAuthorNotFoundException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/authors/getByEmail")
                        .header("x-validation-report", "true")
                        .param("email", "wrongemail@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Author not found with email: wrongemail@example.com"));
    }
}

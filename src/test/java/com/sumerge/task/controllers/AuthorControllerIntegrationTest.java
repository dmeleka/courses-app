package com.sumerge.task.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumerge.task.models.Author;
import com.sumerge.task.repositories.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthorRepository authorRepository;

    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setName("Test Author");
        author.setEmail("author@test.com");
    }

    @AfterEach
    void tearDown() {
        authorRepository.deleteAll();
    }

    @Transactional
    @Test
    public void testAddAuthor() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/authors/addAuthor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(author.getName()))
                .andExpect(jsonPath("$.email").value(author.getEmail()));
    }

    @Transactional
    @Test
    public void getAuthorByEmail_authorFound_shouldReturnAuthorDTO() throws Exception {
        authorRepository.save(author);

        mockMvc.perform(MockMvcRequestBuilders.get("/authors/getAuthorByEmail")
                        .param("email", author.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(author.getName()))
                .andExpect(jsonPath("$.email").value(author.getEmail()));
    }

    @Transactional
    @Test
    public void getAuthorByEmail_authorNotFound_shouldThrowAuthorNotFoundException() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/authors/getAuthorByEmail")
                        .param("email", author.getEmail()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Author not found with email: " + author.getEmail()));
    }
}

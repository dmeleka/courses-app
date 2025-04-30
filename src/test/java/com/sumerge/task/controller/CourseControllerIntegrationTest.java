package com.sumerge.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumerge.task.dto.CourseDTO;
import com.sumerge.task.model.Course;
import com.sumerge.task.model.Lang;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CourseControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddCourse() throws Exception {
        Course course1 = new Course();
        course1.setName("Test Course 1");
        course1.setAuthors(new ArrayList<>());
        course1.setLang(Lang.JAVA);

        mockMvc.perform(MockMvcRequestBuilders.post("/courses/add")
                        .header("x-validation-report", "true")
                        .with(httpBasic("alice@example.com", "password1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(course1.getName()))
                .andExpect(jsonPath("$.lang").value(course1.getLang().name()));
    }

    @Test
    public void testGetAllRecommendedCourses() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/all/0/2")
                        .header("x-validation-report", "true")
                        .with(httpBasic("alice@example.com", "password1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Java Basics"))
                .andExpect(jsonPath("$.content[0].lang").value("JAVA"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].name").value("Spring Boot Advanced"))
                .andExpect(jsonPath("$.content[1].lang").value("JAVA"));
    }

    @Test
    public void getCourseById_courseFound_shouldReturnCourse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/{id}", 1)
                        .header("x-validation-report", "true")
                        .with(httpBasic("alice@example.com", "password1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Java Basics"))
                .andExpect(jsonPath("$.lang").value("JAVA"));
    }

    @Test
    public void getCourseById_courseNotFound_shouldThrowCourseNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/999")
                        .header("x-validation-report", "true"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course not found with id: 999"));
    }

    @Test
    public void deleteCourseById_courseFound_shouldDeleteCourse() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/delete/{id}", 1)
                        .header("x-validation-report", "true")
                        .with(httpBasic("alice@example.com", "password1")))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted course"));
    }

    @Test
    public void deleteCourseById_courseNotFound_shouldThrowCourseNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/delete/999")
                        .header("x-validation-report", "true")
                        .with(httpBasic("alice@example.com", "password1")))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course not found with id: 999"));
    }

    @Test
    public void updateCourseById_courseFound_shouldUpdateCourse() throws Exception {
        CourseDTO courseDTO1 = new CourseDTO();
        courseDTO1.setName("Test Course 1 DTO");
        courseDTO1.setLang(Lang.JAVA);

        mockMvc.perform(MockMvcRequestBuilders.put("/courses/update/{id}", 1)
                        .header("x-validation-report", "true")
                        .with(httpBasic("alice@example.com", "password1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(courseDTO1.getName()))
                .andExpect(jsonPath("$.lang").value(courseDTO1.getLang().name()));
    }

    @Test
    public void updateCourseById_courseNotFound_shouldThrowCourseNotFoundException() throws Exception {
        CourseDTO courseDTO1 = new CourseDTO();
        courseDTO1.setName("Test Course 1 DTO");
        courseDTO1.setLang(Lang.JAVA);

        mockMvc.perform(MockMvcRequestBuilders.put("/courses/update/999")
                        .header("x-validation-report", "true")
                        .with(httpBasic("alice@example.com", "password1"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO1)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course not found with id: 999"));
    }

    @Test
    public void addAuthorToCourse_isOwner_shouldAddAuthorToCourse() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/courses/addAuthor/{cid}", 2)
                        .header("x-validation-report", "true")
                        .with(httpBasic("bob@example.com", "password2"))
                        .param("email", "daniel@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Spring Boot Advanced"))
                .andExpect(jsonPath("$.lang").value("JAVA"));
    }

    @Test
    public void addAuthorToCourse_isNotOwner_shouldThrowNotCourseOwnerException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/courses/addAuthor/{cid}", 1)
                        .header("x-validation-report", "true")
                        .with(httpBasic("daniel@example.com", "password4"))
                        .param("email", "daniel@example.com"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("You are not the owner of this course"));
    }

    @Test
    public void addAuthorToCourse_courseNotFound_shouldThrowCourseNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/courses/addAuthor/{cid}", 999)
                        .header("x-validation-report", "true")
                        .with(httpBasic("alice@example.com", "password1"))
                        .param("email", "daniel@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course not found with id: 999"));
    }

    @Test
    public void addAuthorToCourse_authorNotFound_shouldThrowAuthorNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/courses/addAuthor/{cid}", 1)
                        .header("x-validation-report", "true")
                        .with(httpBasic("alice@example.com", "password1"))
                        .param("email", "wrongemail@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Author not found with email: wrongemail@example.com"));
    }

}

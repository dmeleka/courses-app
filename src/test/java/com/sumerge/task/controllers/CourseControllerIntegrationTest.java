package com.sumerge.task.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumerge.task.dtos.CourseDTO;
import com.sumerge.task.models.Course;
import com.sumerge.task.models.Lang;
import com.sumerge.task.repositories.CourseRepository;
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

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseRepository courseRepository;

    private Course course1;
    private CourseDTO courseDTO1;

    private Course course2;
    private CourseDTO courseDTO2;


    @BeforeEach
    void setUp() {
        course1 = new Course();
        course1.setName("Test Course 1");
        course1.setAuthors(new ArrayList<>());
        course1.setLang(Lang.JAVA);

        courseDTO1 = new CourseDTO();
        courseDTO1.setName("Test Course 1 DTO");
        courseDTO1.setLang(Lang.JAVA);

        course2 = new Course();
        course2.setName("Test Course 2");
        course2.setAuthors(new ArrayList<>());
        course2.setLang(Lang.JAVASCRIPT);

        courseDTO2 = new CourseDTO();
        courseDTO2.setName("Test Course 2 DTO");
        courseDTO2.setLang(Lang.JAVASCRIPT);

    }

    @AfterEach
    void tearDown() {
        courseRepository.deleteAll();
    }

    @Transactional
    @Test
    public void testAddCourse() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/courses/addCourse")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(course1.getName()))
                .andExpect(jsonPath("$.lang").value(course1.getLang().name()));
    }

    @Transactional
    @Test
    public void testGetAllRecommendedCourses() throws Exception {

        long c1Id = courseRepository.save(course1).getId();
        long c2Id = courseRepository.save(course2).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/allCourses/0/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(c1Id))
                .andExpect(jsonPath("$.content[0].name").value(course1.getName()))
                .andExpect(jsonPath("$.content[0].lang").value(course1.getLang().name()))
                .andExpect(jsonPath("$.content[1].id").value(c2Id))
                .andExpect(jsonPath("$.content[1].name").value(course2.getName()))
                .andExpect(jsonPath("$.content[1].lang").value(course2.getLang().name()));
    }

    @Test
    public void getCourseById_courseFound_shouldReturnCourse() throws Exception {
        long c1Id = courseRepository.save(course1).getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/courses/{id}", c1Id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(c1Id))
                .andExpect(jsonPath("$.name").value(course1.getName()))
                .andExpect(jsonPath("$.lang").value(course1.getLang().name()));
    }

    @Test
    public void getCourseById_courseNotFound_shouldThrowCourseNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/courses/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course not found with id: 999"));
    }

    @Test
    public void deleteCourseById_courseFound_shouldDeleteCourse() throws Exception {
        long c1Id = courseRepository.save(course1).getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/{id}", c1Id))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted course"));
    }

    @Test
    public void deleteCourseById_courseNotFound_shouldThrowCourseNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/courses/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course not found with id: 999"));
    }

    @Test
    public void updateCourseById_courseFound_shouldUpdateCourse() throws Exception {
        long c1Id = courseRepository.save(course1).getId();

        mockMvc.perform(MockMvcRequestBuilders.put("/courses/{id}", c1Id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(c1Id))
                .andExpect(jsonPath("$.name").value(courseDTO2.getName()))
                .andExpect(jsonPath("$.lang").value(courseDTO2.getLang().name()));
    }

    @Test
    public void updateCourseById_courseNotFound_shouldThrowCourseNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/courses/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO1)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course not found with id: 999"));
    }
}

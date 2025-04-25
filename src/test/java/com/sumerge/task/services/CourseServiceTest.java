package com.sumerge.task.services;

import com.sumerge.task.dtos.CourseDTO;
import com.sumerge.task.exceptions.CourseNotFoundException;
import com.sumerge.task.mappers.CourseMapper;
import com.sumerge.task.models.Author;
import com.sumerge.task.models.Course;
import com.sumerge.task.repositories.AuthorRepository;
import com.sumerge.task.repositories.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private CourseRecommender javascriptCourseRecommender;

    @Mock
    private CourseRecommender javaCourseRecommender;

    private CourseService courseService;

    private Course course;
    private CourseDTO courseDTO;
    private Author author;

    @BeforeEach
    public void init() {

        courseService = new CourseService(javaCourseRecommender, courseRepository, courseMapper);
        courseService.setCourseRecommender(javascriptCourseRecommender);
        courseService.setAuthorRepository(authorRepository);

        author = new Author();
        author.setId(1L);
        author.setName("Test Author");
        author.setEmail("author@example.com");

        course = new Course();
        course.setId(1L);
        course.setName("Test Course");
        course.setAuthors(new ArrayList<>(Collections.singleton((author))));

        courseDTO = new CourseDTO();
        courseDTO.setId(1L);
        courseDTO.setName("Test Course DTO");
    }

    @Test
    public void addCourse_courseIsSaved_expectedDTOReturned() {

        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toDTO(course)).thenReturn(courseDTO);

        CourseDTO output = courseService.addCourse(course);

        assertNotNull(output);
        assertAll("courseAttributes",
                () -> assertEquals(courseDTO.getId(), output.getId()),
                () -> assertEquals(courseDTO.getName(), output.getName())
        );

    }

    @Test
    public void getCourseById_courseExists_expectedDTOReturned() {

        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        when(courseMapper.toDTO(course)).thenReturn(courseDTO);

        CourseDTO output = courseService.getCourseById(course.getId());

        assertNotNull(output);
        assertAll("Attributes",
                () -> assertEquals(courseDTO.getId(), output.getId()),
                () -> assertEquals(courseDTO.getName(), output.getName())
        );
    }

    @Test
    public void getCourseById_courseNotFound_CourseNotFoundExceptionThrown() {

        when(courseRepository.findById(course.getId())).thenReturn(Optional.empty());

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> {
            courseService.getCourseById(course.getId());
        });
        assertEquals("Course not found with id: " + course.getId(), exception.getMessage());
    }

    @Test
    public void getAllCourses_coursesExist_expectedPageWithDTOsReturned() {

        List<Course> javaCourses = List.of(course);
        List<Course> jsCourses = List.of(course);

        when(javaCourseRecommender.recommendedCourses()).thenReturn(javaCourses);
        when(javascriptCourseRecommender.recommendedCourses()).thenReturn(jsCourses);

        when(courseMapper.toDTO(course)).thenReturn(courseDTO);
        when(courseMapper.toDTO(course)).thenReturn(courseDTO);

        Page<CourseDTO> output = courseService.getAllCourses(0, 2);

        assertEquals(2, output.getTotalElements());
        assertEquals(1, output.getTotalPages());
        assertEquals(2, output.getContent().size());

        assertAll("Attributes",
                () -> assertEquals(courseDTO.getId(), output.getContent().get(0).getId()),
                () -> assertEquals(courseDTO.getName(), output.getContent().get(0).getName()),

                () -> assertEquals(courseDTO.getId(), output.getContent().get(1).getId()),
                () -> assertEquals(courseDTO.getName(), output.getContent().get(1).getName())
        );
    }

    @Test
    public void updateCourse_courseExists_shouldUpdateAndReturnUpdatedDTO() {

        CourseDTO updatedCourse = new CourseDTO();
        updatedCourse.setId(1L);
        updatedCourse.setName("Test Course Updated");

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("author@example.com");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authorRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        doNothing().when(courseMapper).updateCourseFromDTO(updatedCourse, course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toDTO(course)).thenReturn(updatedCourse);

        CourseDTO output = courseService.updateCourse(course.getId(), updatedCourse);

        assertNotNull(output);
        assertAll("Attributes",
                () -> assertEquals(updatedCourse.getId(), output.getId()),
                () -> assertEquals(updatedCourse.getName(), output.getName())
        );
    }

    @Test
    public void updateCourse_courseNotFound_CourseNotFoundExceptionThrown() {

        CourseDTO updatedCourse = new CourseDTO();
        updatedCourse.setId(1L);
        updatedCourse.setName("Test Course Updated");


        when(courseRepository.findById(course.getId())).thenReturn(Optional.empty());

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> {
            courseService.updateCourse(course.getId(), updatedCourse);
        });
        assertEquals("Course not found with id: " + course.getId(), exception.getMessage());
    }

    @Test
    public void deleteCourseById_courseExists_courseDeleted() {
        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("author@example.com");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authorRepository.findByEmail(author.getEmail())).thenReturn(Optional.of(author));
        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        doNothing().when(courseRepository).delete(course);

        courseService.deleteCourseById(course.getId());

        verify(courseRepository).findById(course.getId());
        verify(courseRepository).delete(course);
    }

    @Test
    public void deleteCourseById_courseNotFound_CourseNotFoundExceptionThrown() {
        when(courseRepository.findById(course.getId())).thenReturn(Optional.empty());

        CourseNotFoundException exception = assertThrows(CourseNotFoundException.class, () -> {
            courseService.deleteCourseById(course.getId());
        });
        assertEquals("Course not found with id: " + course.getId(), exception.getMessage());

        verify(courseRepository).findById(course.getId());
    }
}

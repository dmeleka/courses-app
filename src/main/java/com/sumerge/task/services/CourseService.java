package com.sumerge.task.services;

import com.sumerge.task.dtos.CourseDTO;
import com.sumerge.task.exceptions.AuthorNotFoundException;
import com.sumerge.task.exceptions.CourseNotFoundException;
import com.sumerge.task.exceptions.NotCourseOwnerException;
import com.sumerge.task.mappers.CourseMapper;
import com.sumerge.task.models.Author;
import com.sumerge.task.models.Course;
import com.sumerge.task.repositories.AuthorRepository;
import com.sumerge.task.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CourseService {

    CourseRepository courseRepository;
    AuthorRepository authorRepository;
    CourseMapper courseMapper;
    CourseRecommender javaCourseRecommender;
    CourseRecommender javascriptCourseRecommender;

    @Autowired
    public CourseService(CourseRecommender javaCourseRecommender, CourseRepository courseRepository, CourseMapper courseMapper) {
        this.javaCourseRecommender = javaCourseRecommender;
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Autowired
    public void setAuthorRepository(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Autowired
    public void setCourseRecommender(@Qualifier("javascriptCourseRecommender") CourseRecommender javascriptCourseRecommender) {
        this.javascriptCourseRecommender = javascriptCourseRecommender;
    }

    public CourseDTO addCourse(@RequestBody Course course) {
        return courseMapper.toDTO(courseRepository.save(course));
    }

    public CourseDTO getCourseById(long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));
        return courseMapper.toDTO(course);
    }

    public Page<CourseDTO> getAllCourses(int page, int size) {
        List<Course> javaCourses = javaCourseRecommender.recommendedCourses();
        List<Course> jsCourses = javascriptCourseRecommender.recommendedCourses();

        List<Course> allCourses = new ArrayList<>();
        allCourses.addAll(javaCourses);
        allCourses.addAll(jsCourses);

        int start = page * size;
        int end = Math.min(start + size, allCourses.size());

        if (start > end) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page, size), allCourses.size());
        }

        List<CourseDTO> paginated = allCourses.subList(start, end).stream().map(courseMapper::toDTO).toList();

        return new PageImpl<>(paginated, PageRequest.of(page, size), allCourses.size());
    }

    public void deleteCourseById(long cid) {
        Course course = courseRepository.findById(cid).orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + cid));
        course.getAuthors().clear();
        courseRepository.save(course);
        courseRepository.delete(course);
    }

    public CourseDTO updateCourse(long cid, CourseDTO courseDTO) {
        Course course = courseRepository.findById(cid).orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + cid));
        courseMapper.updateCourseFromDTO(courseDTO, course);
        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toDTO(updatedCourse);
    }

    public CourseDTO addAuthorToCourse(long cid, String email) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String authorizedEmail;

        if (principal instanceof UserDetails) {
            authorizedEmail = ((UserDetails) principal).getUsername();
        } else {
            authorizedEmail = principal.toString();
        }

        Author authorizedAuthor = authorRepository.findByEmail(authorizedEmail)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with email: " + email));

        Author toAddAuthor = authorRepository.findByEmail(email)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with email: " + email));

        Course course = courseRepository.findById(cid)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + cid));

        boolean isOwner = course.getAuthors().stream()
                .anyMatch(a -> a.getId().equals(authorizedAuthor.getId()));

        if (!isOwner) {
            throw new NotCourseOwnerException();
        }

        course.getAuthors().add(toAddAuthor);
        toAddAuthor.getCourses().add(course);

        courseRepository.save(course);
        authorRepository.save(toAddAuthor);

        return courseMapper.toDTO(course);
    }
}

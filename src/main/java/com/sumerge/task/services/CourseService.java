package com.sumerge.task.services;

import com.sumerge.task.dtos.CourseDTO;
import com.sumerge.task.mappers.CourseMapper;
import com.sumerge.task.models.Course;
import com.sumerge.task.repositories.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CourseService {

    CourseRepository courseRepository;
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
    public void setCourseRecommender(@Qualifier("javascriptCourseRecommender") CourseRecommender ratingBasedCourseRecommender) {
        this.javascriptCourseRecommender = ratingBasedCourseRecommender;
    }

    public CourseDTO addCourse(@RequestBody Course course) {
        return courseMapper.toDTO(courseRepository.save(course));
    }

    public CourseDTO getCourseById(long id) {
        return courseMapper.toDTO(courseRepository.findById(id).get());
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
        Course course = courseRepository.findById(cid).orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + cid));
        course.getAuthors().clear();
        courseRepository.save(course);
        courseRepository.delete(course);
    }

    public CourseDTO updateCourse(long cid, CourseDTO courseDTO) {
        Course course = courseRepository.findById(cid).orElseThrow(() -> new RuntimeException("Course not found"));
        courseMapper.updateCourseFromDTO(courseDTO, course);
        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toDTO(updatedCourse);
    }

}

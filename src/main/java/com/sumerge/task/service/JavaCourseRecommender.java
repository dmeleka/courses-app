package com.sumerge.task.service;

import com.sumerge.task.model.Course;
import com.sumerge.task.model.Lang;
import com.sumerge.task.repository.CourseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary //comment @Primary to utilize precedence by variable name
public class JavaCourseRecommender implements CourseRecommender {

    private final CourseRepository courseRepository;

    @Autowired
    public JavaCourseRecommender(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> recommendedCourses() {
        return courseRepository.findByLang(Lang.JAVA);
    }
}
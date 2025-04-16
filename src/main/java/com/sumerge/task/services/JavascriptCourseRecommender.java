package com.sumerge.task.services;

import com.sumerge.task.models.Course;
import com.sumerge.task.models.Lang;
import com.sumerge.task.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JavascriptCourseRecommender implements CourseRecommender {

    CourseRepository courseRepository;

    @Autowired
    public JavascriptCourseRecommender(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> recommendedCourses() {
        return courseRepository.findByLang(Lang.JAVASCRIPT);
    }

}
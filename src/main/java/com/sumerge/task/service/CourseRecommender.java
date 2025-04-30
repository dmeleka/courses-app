package com.sumerge.task.service;

import com.sumerge.task.model.Course;

import java.util.List;

public interface CourseRecommender {
    List<Course> recommendedCourses();
}

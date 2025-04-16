package com.sumerge.task.services;

import com.sumerge.task.models.Course;
import java.util.List;

public interface CourseRecommender {
    List<Course> recommendedCourses();
}

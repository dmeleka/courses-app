package com.sumerge.task.repository;

import com.sumerge.task.model.Course;
import com.sumerge.task.model.Lang;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByLang(Lang lang);
}

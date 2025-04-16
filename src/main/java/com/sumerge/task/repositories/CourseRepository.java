package com.sumerge.task.repositories;

import com.sumerge.task.models.Course;
import com.sumerge.task.models.Lang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByLang(Lang lang);
}

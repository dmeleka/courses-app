package com.sumerge.task.controllers;

import com.sumerge.task.dtos.CourseDTO;
import com.sumerge.task.services.CourseService;
import com.sumerge.task.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/courses")
@RestController
public class CourseController {
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/addCourse")
    public ResponseEntity<?> addCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.addCourse(course));
    }

    @GetMapping("/allCourses/{page}/{size}")
    public ResponseEntity<?> getAllRecommendedCourses(@PathVariable int page, @PathVariable int size) {
        return ResponseEntity.ok(courseService.getAllCourses(page, size));
    }

    @GetMapping("/{cid}")
    public ResponseEntity<?> getCourseById(@PathVariable long cid) {
        return ResponseEntity.ok(courseService.getCourseById(cid));
    }

    @DeleteMapping("/{cid}")
    public ResponseEntity<?> deleteCourseById(@PathVariable long cid) {
        courseService.deleteCourseById(cid);
        return ResponseEntity.ok("Deleted course");
    }

    @PutMapping("/{cid}")
    public ResponseEntity<?> getCourseById(@PathVariable Long cid, @RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(courseService.updateCourse(cid, courseDTO));
    }

}
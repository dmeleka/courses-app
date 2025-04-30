package com.sumerge.task.controller;

import com.sumerge.task.dto.CourseDTO;
import com.sumerge.task.model.Course;
import com.sumerge.task.service.CourseService;

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

    @PostMapping("/add")
    public ResponseEntity<?> addCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.addCourse(course));
    }

    @GetMapping("/all/{page}/{size}")
    public ResponseEntity<?> getAllRecommendedCourses(@PathVariable int page, @PathVariable int size) {
        return ResponseEntity.ok(courseService.getAllCourses(page, size));
    }

    @GetMapping("/{cid}")
    public ResponseEntity<?> getCourseById(@PathVariable long cid) {
        return ResponseEntity.ok(courseService.getCourseById(cid));
    }

    @DeleteMapping("/delete/{cid}")
    public ResponseEntity<?> deleteCourseById(@PathVariable long cid) {
        courseService.deleteCourseById(cid);
        return ResponseEntity.ok("Deleted course");
    }

    @PutMapping("/update/{cid}")
    public ResponseEntity<?> getCourseById(@PathVariable Long cid, @RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(courseService.updateCourse(cid, courseDTO));
    }

    @PutMapping("/addAuthor/{cid}")
    public ResponseEntity<?> addAuthor(@PathVariable long cid, @RequestParam String email) {
        return ResponseEntity.ok(courseService.addAuthorToCourse(cid, email));
    }

    @GetMapping("/discover")
    public ResponseEntity<?> getCourses() {
        return ResponseEntity.ok(courseService.coursesXML());
    }
}
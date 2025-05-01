package com.sumerge.task.controller;

import com.sumerge.task.dto.CourseDTO;
import com.sumerge.task.model.Course;
import com.sumerge.task.service.CourseService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/courses")
@RestController
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course added"),
            @ApiResponse(responseCode = "401", description = "Unauthenticated Author or invalid/missing x-validation-report header")
    })
    @PostMapping("/")
    public ResponseEntity<?> addCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.addCourse(course));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return page of courses. Page might be empty"),
            @ApiResponse(responseCode = "401", description = "invalid/missing x-validation-report header")
    })
    @GetMapping("/{page}/{size}")
    public ResponseEntity<?> getAllRecommendedCourses(@PathVariable int page, @PathVariable int size) {
        System.out.println("reached getAllRecommendedCourses");
        return ResponseEntity.ok(courseService.getAllCourses(page, size));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course returned"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "401", description = "invalid/missing x-validation-report header")
    })
    @GetMapping("/{cid}")
    public ResponseEntity<?> getCourseById(@PathVariable long cid) {
        return ResponseEntity.ok(courseService.getCourseById(cid));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course deleted"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "401", description = "invalid/missing x-validation-report header")
    })
    @DeleteMapping("/{cid}")
    public ResponseEntity<?> deleteCourseById(@PathVariable long cid) {
        courseService.deleteCourseById(cid);
        return ResponseEntity.ok("Deleted course");
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "401", description = "invalid/missing x-validation-report header")
    })
    @PutMapping("/{cid}")
    public ResponseEntity<?> getCourseById(@PathVariable Long cid, @RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok(courseService.updateCourse(cid, courseDTO));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Author added to Course"),
            @ApiResponse(responseCode = "404", description = "Author or Course not found"),
            @ApiResponse(responseCode = "401", description = "Authenticated Author is not user or invalid/missing x-validation-report header")
    })
    @PutMapping("/{cid}/author")
    public ResponseEntity<?> addAuthor(@PathVariable long cid, @RequestParam String email) {
        return ResponseEntity.ok(courseService.addAuthorToCourse(cid, email));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of courses returned from external SoapUI server")
    })
    @GetMapping("/external")
    public ResponseEntity<?> getCourses() {
        return ResponseEntity.ok(courseService.coursesXML());
    }
}
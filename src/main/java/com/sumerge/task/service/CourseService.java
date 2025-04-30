package com.sumerge.task.service;

import com.example.course.CoursesXSD;
import com.sumerge.task.client.CourseClient;
import com.sumerge.task.dto.CourseDTO;
import com.sumerge.task.exception.AuthorNotFoundException;
import com.sumerge.task.exception.CourseNotFoundException;
import com.sumerge.task.exception.NotCourseOwnerException;
import com.sumerge.task.mapper.CourseMapper;
import com.sumerge.task.model.Author;
import com.sumerge.task.model.Course;
import com.sumerge.task.repository.AuthorRepository;
import com.sumerge.task.repository.CourseRepository;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CourseService {

    CourseRepository courseRepository;
    AuthorRepository authorRepository;
    CourseMapper courseMapper;
    CourseClient courseClient;
    CourseRecommender javaCourseRecommender;
    CourseRecommender javascriptCourseRecommender;

    @Autowired
    public CourseService(CourseRecommender javaCourseRecommender, CourseRepository courseRepository, CourseMapper courseMapper) {
        this.javaCourseRecommender = javaCourseRecommender;
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    @Autowired
    public void setCourseClient (CourseClient courseClient) {
        this.courseClient = courseClient;
    }

    @Autowired
    public void setAuthorRepository(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Autowired
    public void setCourseRecommender(@Qualifier("javascriptCourseRecommender") CourseRecommender javascriptCourseRecommender) {
        this.javascriptCourseRecommender = javascriptCourseRecommender;
    }

    public CourseDTO addCourse(@RequestBody Course course) {
        return courseMapper.toDTO(courseRepository.save(course));
    }

    public CourseDTO getCourseById(long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + id));
        return courseMapper.toDTO(course);
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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        Course course = courseRepository.findById(cid)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + cid));

        Author author = authorRepository.findByEmail(email)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with email: " + email));

        boolean isOwner = course.getAuthors().stream()
                .anyMatch(a -> a.getId().equals(author.getId()));

        if (!isOwner) {
            throw new NotCourseOwnerException();
        }

        courseRepository.delete(course);
    }

    public CourseDTO updateCourse(long cid, CourseDTO courseDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        Course course = courseRepository.findById(cid)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + cid));

        Author author = authorRepository.findByEmail(email)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with email: " + email));

        boolean isOwner = course.getAuthors().stream()
                .anyMatch(a -> a.getId().equals(author.getId()));

        if (!isOwner) {
            throw new NotCourseOwnerException();
        }

        courseMapper.updateCourseFromDTO(courseDTO, course);
        Course updatedCourse = courseRepository.save(course);
        return courseMapper.toDTO(updatedCourse);
    }

    public CourseDTO addAuthorToCourse(long cid, String email) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String authorizedEmail;

        if (principal instanceof UserDetails) {
            authorizedEmail = ((UserDetails) principal).getUsername();
        } else {
            authorizedEmail = principal.toString();
        }

        Author authorizedAuthor = authorRepository.findByEmail(authorizedEmail)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with email: " + email));

        Author toAddAuthor = authorRepository.findByEmail(email)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with email: " + email));

        Course course = courseRepository.findById(cid)
                .orElseThrow(() -> new CourseNotFoundException("Course not found with id: " + cid));

        boolean isOwner = course.getAuthors().stream()
                .anyMatch(a -> a.getId().equals(authorizedAuthor.getId()));

        if (!isOwner) {
            throw new NotCourseOwnerException();
        }

        course.getAuthors().add(toAddAuthor);
        toAddAuthor.getCourses().add(course);

        courseRepository.save(course);
        authorRepository.save(toAddAuthor);

        return courseMapper.toDTO(course);
    }

    public List<CoursesXSD.CourseXSD> coursesXML () {
        return parseCoursesXml(courseClient.coursesXML());
    }

    public static List<CoursesXSD.CourseXSD> parseCoursesXml(String xml) {
        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(CoursesXSD.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            StringReader reader = new StringReader(xml);
            CoursesXSD courses = (CoursesXSD) unmarshaller.unmarshal(reader);
            List<CoursesXSD.CourseXSD> courseList = courses.getCourseXSD();
            System.out.println("Number of courses: " + (courseList != null ? courseList.size() : "0"));
            return courseList;
        } catch (JAXBException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }



}

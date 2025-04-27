package com.sumerge.task;

import com.sumerge.task.models.Assessment;
import com.sumerge.task.models.Course;
import com.sumerge.task.models.Rating;
import com.sumerge.task.models.Author;
import com.sumerge.task.models.Lang;
import com.sumerge.task.repositories.AssessmentRepository;
import com.sumerge.task.repositories.AuthorRepository;
import com.sumerge.task.repositories.CourseRepository;
import com.sumerge.task.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AuthorRepository authorRepository;
    private final CourseRepository courseRepository;
    private final AssessmentRepository assessmentRepository;
    private final RatingRepository ratingRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(AuthorRepository authorRepository, CourseRepository courseRepository,
                           AssessmentRepository assessmentRepository, RatingRepository ratingRepository,
                           PasswordEncoder passwordEncoder) {
        this.authorRepository = authorRepository;
        this.courseRepository = courseRepository;
        this.assessmentRepository = assessmentRepository;
        this.ratingRepository = ratingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        Author alice = new Author(null, "Alice Smith", "alice@example.com", passwordEncoder.encode("password1"), LocalDate.of(1985, 3, 12), new ArrayList<>());
        Author bob = new Author(null, "Bob Johnson", "bob@example.com", passwordEncoder.encode("password2"), LocalDate.of(1978, 7, 25), new ArrayList<>());
        Author clara = new Author(null, "Clara Nguyen", "clara@example.com", passwordEncoder.encode("password3"), LocalDate.of(1990, 9, 18), new ArrayList<>());
        Author daniel = new Author(null, "Daniel Lee", "daniel@example.com", passwordEncoder.encode("password4"), LocalDate.of(1983, 1, 4), new ArrayList<>());

        authorRepository.saveAll(List.of(alice, bob, clara, daniel));

        Course c1 = new Course(0, "Java Basics", "Intro to Java programming", 3, Lang.JAVA, null, new ArrayList<>(), new ArrayList<>());
        Course c2 = new Course(0, "Spring Boot Advanced", "Deep dive into Spring Boot", 4, Lang.JAVA, null, new ArrayList<>(), new ArrayList<>());
        Course c3 = new Course(0, "JavaScript Essentials", "Basics of JS", 2, Lang.JAVASCRIPT, null, new ArrayList<>(), new ArrayList<>());
        Course c4 = new Course(0, "Fullstack App with React and Spring", "Building a full app", 5, Lang.JAVA, null, new ArrayList<>(), new ArrayList<>());
        Course c5 = new Course(0, "Node.js Backend Fundamentals", "Learn Node.js basics", 3, Lang.JAVASCRIPT, null, new ArrayList<>(), new ArrayList<>());

        c1.setAuthors(List.of(alice, bob));
        c2.setAuthors(List.of(bob));
        c3.setAuthors(List.of(clara));
        c4.setAuthors(List.of(alice, daniel));
        c5.setAuthors(List.of(clara, daniel));

        courseRepository.saveAll(List.of(c1, c2, c3, c4, c5));

        Assessment assessment1 = new Assessment();
        assessment1.setContent("Midterm Assessment for Java Basics");
        assessment1.setCourse(c1);

        Assessment assessment2 = new Assessment();
        assessment2.setContent("Final Assessment for Spring Boot Advanced");
        assessment2.setCourse(c2);

        Assessment assessment3 = new Assessment();
        assessment3.setContent("Final Assessment for JavaScript Essentials");
        assessment3.setCourse(c3);

        Assessment assessment4 = new Assessment();
        assessment4.setContent("Midterm Assessment for Fullstack App with React and Spring");
        assessment4.setCourse(c4);

        Assessment assessment5 = new Assessment();
        assessment5.setContent("Final Assessment for Node.js Backend Fundamentals");
        assessment5.setCourse(c5);

        assessmentRepository.saveAll(List.of(assessment1, assessment2, assessment3, assessment4, assessment5));

        Rating rating1 = new Rating();
        rating1.setNumber(5);
        rating1.setCourse(c1);

        Rating rating2 = new Rating();
        rating2.setNumber(4);
        rating2.setCourse(c2);

        Rating rating3 = new Rating();
        rating3.setNumber(3);
        rating3.setCourse(c3);

        Rating rating4 = new Rating();
        rating4.setNumber(4);
        rating4.setCourse(c4);

        Rating rating5 = new Rating();
        rating5.setNumber(5);
        rating5.setCourse(c5);

        ratingRepository.saveAll(List.of(rating1, rating2, rating3, rating4, rating5));
    }
}
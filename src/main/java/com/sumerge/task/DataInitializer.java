package com.sumerge.task;

import com.sumerge.task.models.Author;
import com.sumerge.task.models.Course;
import com.sumerge.task.models.Lang;
import com.sumerge.task.repositories.AuthorRepository;
import com.sumerge.task.repositories.CourseRepository;
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

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(AuthorRepository authorRepository, CourseRepository courseRepository, PasswordEncoder passwordEncoder) {
        this.authorRepository = authorRepository;
        this.courseRepository = courseRepository;
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
    }

}
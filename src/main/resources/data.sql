-- Step 1: Clean tables (in reverse dependency order)
DELETE FROM course_author;
DELETE FROM rating;
DELETE FROM assessment;
DELETE FROM author;
DELETE FROM course;

-- Step 2: Insert Authors
INSERT INTO author (id, name, email, birthdate) VALUES
                                                    (1, 'Alice Smith', 'alice@example.com', DATE '1985-03-12'),
                                                    (2, 'Bob Johnson', 'bob@example.com', DATE '1978-07-25'),
                                                    (3, 'Clara Nguyen', 'clara@example.com', DATE '1990-09-18'),
                                                    (4, 'Daniel Lee', 'daniel@example.com', DATE '1983-01-04');

-- Step 3: Insert Courses (with Lang)
INSERT INTO course (id, name, description, credit, lang) VALUES
                                                             (1, 'Java Basics', 'Intro to Java programming', 3, 'JAVA'),
                                                             (2, 'Spring Boot Advanced', 'Deep dive into Spring Boot', 4, 'JAVA'),
                                                             (3, 'JavaScript Essentials', 'Basics of JS', 2, 'JAVASCRIPT'),
                                                             (4, 'Fullstack App with React and Spring', 'Building a full app', 5, 'JAVA'),
                                                             (5, 'Node.js Backend Fundamentals', 'Learn Node.js basics', 3, 'JAVASCRIPT');

-- Step 4: Insert Course-Author Relationships (Many-to-Many)
INSERT INTO course_author (course_id, author_id) VALUES
                                                     (1, 1),
                                                     (1, 2),
                                                     (2, 2),
                                                     (3, 3),
                                                     (4, 1),
                                                     (4, 4),
                                                     (5, 3),
                                                     (5, 4);

-- Step 5: Insert Assessments (One-to-One)
INSERT INTO assessment (id, content, course_id) VALUES
                                                    (1, 'Final exam covering Java syntax and OOP', 1),
                                                    (2, 'Project-based assessment for Spring Boot', 2),
                                                    (3, 'Multiple choice quiz on JS basics', 3),
                                                    (4, 'Mini fullstack project presentation', 4),
                                                    (5, 'API-building assignment using Node.js', 5);

-- Step 6: Insert Ratings (One-to-Many)
INSERT INTO rating (id, number, course_id) VALUES
                                               (1, 5, 1),
                                               (2, 4, 1),
                                               (3, 5, 2),
                                               (4, 3, 3),
                                               (5, 4, 3),
                                               (6, 5, 4),
                                               (7, 5, 4),
                                               (8, 4, 5),
                                               (9, 3, 5);

ALTER TABLE course ALTER COLUMN id RESTART WITH 6;
ALTER TABLE author ALTER COLUMN id RESTART WITH 5;
ALTER TABLE assessment ALTER COLUMN id RESTART WITH 6;
ALTER TABLE rating ALTER COLUMN id RESTART WITH 10;
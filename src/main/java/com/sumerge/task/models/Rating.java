package com.sumerge.task.models;

import jakarta.persistence.*;

@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int number;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
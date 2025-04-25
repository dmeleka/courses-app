package com.sumerge.task.exceptions;

public class NotCourseOwnerException extends RuntimeException {

    public NotCourseOwnerException() {
        super("You are not the owner of this course");
    }
}

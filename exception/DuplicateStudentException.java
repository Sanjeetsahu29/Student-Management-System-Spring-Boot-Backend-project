package com.example.studentmanagement.exception;

public class DuplicateStudentException extends RuntimeException {
    public DuplicateStudentException(String email) {
        super("Student already exists with email: " + email);
    }
}

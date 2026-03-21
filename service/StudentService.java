package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StudentRequestDTO;
import com.example.studentmanagement.dto.StudentResponseDTO;

import java.util.List;

public interface StudentService {
    StudentResponseDTO createStudent(StudentRequestDTO studentRequestDTO);
    StudentResponseDTO getStudentById(Long id);
    List<StudentResponseDTO> getAllStudents();
    StudentResponseDTO updateStudent(Long id, StudentRequestDTO studentRequestDTO);
    void deleteStudent(Long id);
}

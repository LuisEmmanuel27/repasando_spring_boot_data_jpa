package com.alibou.example.student;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class StudentService {
  private final StudentRepository studentRepository;
  private final StudentMapper studentMapper;

  public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
    this.studentRepository = studentRepository;
    this.studentMapper = studentMapper;
  }

  public StudentResponseDto saveStudent(StudentDto dto) {
    var student = studentMapper.toStudent(dto);
    var savedStudent = studentRepository.save(student);
    return studentMapper.studentResponseDto(savedStudent);
  }

  public List<StudentResponseDto> getAllStudents() {
    return studentRepository.findAll()
        .stream()
        .map(studentMapper::studentResponseDto)
        .collect(Collectors.toList());
  }

  public StudentResponseDto getStudentById(Integer id) {
    return studentRepository.findById(id)
        .map(studentMapper::studentResponseDto)
        .orElse(null);
  }

  public List<StudentResponseDto> getStudentsByName(String name) {
    return studentRepository.findByName(name)
        .stream()
        .map(studentMapper::studentResponseDto)
        .collect(Collectors.toList());
  }

  public void deleteStudent(Integer id) {
    studentRepository.deleteById(id);
  }
}

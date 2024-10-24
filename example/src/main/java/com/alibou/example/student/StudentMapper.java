package com.alibou.example.student;

import org.springframework.stereotype.Service;

import com.alibou.example.school.School;

@Service
public class StudentMapper {

  // Metodo para crear el Estudiante en base al DTO
  public Student toStudent(StudentDto dto) {
    if (dto == null)
      throw new NullPointerException("The studentDto should not be null");

    var student = new Student();
    student.setName(dto.name());
    student.setLastname(dto.lastname());
    student.setEmail(dto.email());

    var school = new School();
    school.setId(dto.SchoolId());

    student.setSchool(school);

    return student;
  }

  // Metodo para responder con el Estudiante en base al DTO
  public StudentResponseDto studentResponseDto(Student student) {
    return new StudentResponseDto(student.getName(), student.getLastname(), student.getEmail());
  }
}

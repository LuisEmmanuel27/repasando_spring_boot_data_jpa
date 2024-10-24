package com.alibou.example.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StudentMapperTest {
  private StudentMapper studentMapper;

  @BeforeEach
  void setUp() {
    studentMapper = new StudentMapper();
  }

  @Test
  void shouldMapStudentDtoToStudent() {
    StudentDto studentDto = new StudentDto(
        "John", "Doe", "john@doe.com", 1);

    Student student = studentMapper.toStudent(studentDto);

    assertEquals(studentDto.name(), student.getName());
    assertEquals(studentDto.lastname(), student.getLastname());
    assertEquals(studentDto.email(), student.getEmail());
    assertNotNull(student.getSchool());
    assertEquals(studentDto.SchoolId(), student.getSchool().getId());
  }

  @Test
  void shoud_throw_null_pointer_exception_when_studentDto_is_null() {
    var exp = assertThrows(NullPointerException.class, () -> studentMapper.toStudent(null));
    assertEquals("The studentDto should not be null", exp.getMessage());
  }

  @Test
  void shouldMapStudentToStudentResponseDto() {
    // Given
    Student student = new Student("Jhon", "Doe", "jhon@doe.com", 27);

    // When
    StudentResponseDto studentResponseDto = studentMapper.studentResponseDto(student);

    // Then
    assertEquals(student.getName(), studentResponseDto.name());
    assertEquals(student.getLastname(), studentResponseDto.lastname());
    assertEquals(student.getEmail(), studentResponseDto.email());
  }
}

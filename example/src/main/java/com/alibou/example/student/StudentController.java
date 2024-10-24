package com.alibou.example.student;

import java.util.List;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class StudentController {
  private final StudentService studentService;

  public StudentController(StudentService studentService) {
    this.studentService = studentService;
  }

  @PostMapping("/students")
  public StudentResponseDto createStudent(@RequestBody @Valid StudentDto studentDto) {
    return studentService.saveStudent(studentDto);
  }

  @GetMapping("/students")
  public List<StudentResponseDto> getAllStudents() {
    return studentService.getAllStudents();
  }

  @GetMapping("/students/{student-id}")
  public StudentResponseDto getStudentById(@PathVariable("student-id") Integer id) {
    return studentService.getStudentById(id);
  }

  @GetMapping("/students/search/{student-name}")
  public List<StudentResponseDto> getStudentsByName(@PathVariable("student-name") String name) {
    return studentService.getStudentsByName(name);
  }

  @DeleteMapping("/students/{student-id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteStudent(@PathVariable("student-id") Integer id) {
    studentService.deleteStudent(id);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
    var errors = new HashMap<String, String>();
    exp.getBindingResult().getAllErrors().forEach(error -> {
      var field = ((FieldError) error).getField();
      var errorMessage = error.getDefaultMessage();
      errors.put(field, errorMessage);
    });

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

}

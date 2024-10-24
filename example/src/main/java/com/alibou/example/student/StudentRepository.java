package com.alibou.example.student;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
  // find students by name, otra opción: List<Student> findAllByName(String name);
  List<Student> findByName(String name);
}

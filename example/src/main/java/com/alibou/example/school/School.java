package com.alibou.example.school;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

import com.alibou.example.student.Student;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class School {
  @Id
  @GeneratedValue
  private Integer id;
  private String name;

  @OneToMany(mappedBy = "school")
  @JsonManagedReference
  private List<Student> students;

  // Constructors
  public School() {
  }

  public School(String name) {
    this.name = name;
  }

  // Getters and Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Student> getStudents() {
    return students;
  }

  public void setStudents(List<Student> students) {
    this.students = students;
  }
}

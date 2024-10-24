package com.alibou.example.studentprofile;

import com.alibou.example.student.Student;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class StudentProfile {
  @Id
  @GeneratedValue
  private Integer id;
  private String bio;

  @OneToOne
  @JoinColumn(name = "student_id")
  private Student student;

  // Constructors
  public StudentProfile() {
  }

  public StudentProfile(String bio) {
    this.bio = bio;
  }

  // Getters and Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }
}

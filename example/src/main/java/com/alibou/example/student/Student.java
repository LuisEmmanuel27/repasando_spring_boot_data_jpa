package com.alibou.example.student;

import java.time.LocalDate;

import com.alibou.example.school.School;
import com.alibou.example.studentprofile.StudentProfile;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "T_STUDENT")
public class Student {

  @Id
  @GeneratedValue
  private Integer id;

  @Column(name = "c_fname", length = 20)
  private String name;
  private String lastname;

  @Column(unique = true)
  private String email;
  private int age;

  @Column(name = "created_at", updatable = false)
  private LocalDate createdAt;

  @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
  private StudentProfile studentProfile;

  @ManyToOne
  @JoinColumn(name = "school_id")
  @JsonBackReference
  private School school;

  // Constructors
  public Student() {
  }

  public Student(String name, String lastname, String email, int age) {
    this.name = name;
    this.lastname = lastname;
    this.email = email;
    this.age = age;
    this.createdAt = LocalDate.now();
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

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int getAge() {
    return age;
  }

  public void setAge(int age) {
    this.age = age;
  }

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDate createdAt) {
    this.createdAt = createdAt;
  }

  public StudentProfile getStudentProfile() {
    return studentProfile;
  }

  public void setStudentProfile(StudentProfile studentProfile) {
    this.studentProfile = studentProfile;
  }

  public School getSchool() {
    return school;
  }

  public void setSchool(School school) {
    this.school = school;
  }

  // Methods
  @PrePersist
  public void prePersist() {
    this.createdAt = LocalDate.now();
  }
}

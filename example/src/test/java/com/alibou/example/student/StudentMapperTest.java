package com.alibou.example.student;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StudentMapperTest {
  @BeforeAll
  static void setUpClass() {
    System.out.println("Antes de todos los tests");
  }

  @AfterAll
  static void tearDownClass() {
    System.out.println("Despues de todos los tests");
  }

  @BeforeEach
  void setUp() {
    System.out.println("Antes que cualquier test");
  }

  @AfterEach
  void tearDown() {
    System.out.println("Despues de cada test");
  }

  @Test
  void testMethod1() {
    System.out.println("Mi primer test");
  }

  @Test
  void testMethod2() {
    System.out.println("Mi segundo test");
  }
}

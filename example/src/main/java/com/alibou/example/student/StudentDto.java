package com.alibou.example.student;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record StudentDto(
    @NotEmpty(message = "El nombre no puede estar vacío!") String name,
    @NotEmpty(message = "El apellido no puede estar vacío!") String lastname,
    @NotNull(message = "El email no puede estar vacío!!") @NotEmpty(message = "El email no puede estar vacío!") String email,
    Integer SchoolId) {

}

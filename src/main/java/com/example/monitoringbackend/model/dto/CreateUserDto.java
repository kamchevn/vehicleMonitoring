package com.example.monitoringbackend.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Registration payload. Deliberately carries no role: the role is assigned server side so a client
 * cannot register itself as an administrator.
 */
public record CreateUserDto(
    @NotBlank(message = "Username is required") String username,
    @NotBlank(message = "Password is required") String password,
    @NotBlank(message = "Repeat password is required") String repeatPassword,
    @NotBlank(message = "Email is required") @Email(message = "Email must be valid") String email,
    @NotBlank(message = "Name is required") String name,
    @NotBlank(message = "Surname is required") String surname) {}

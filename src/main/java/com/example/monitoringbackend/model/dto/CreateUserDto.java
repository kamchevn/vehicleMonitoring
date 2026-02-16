package com.example.monitoringbackend.model.dto;

import com.example.monitoringbackend.model.User;
import com.example.monitoringbackend.model.enumerations.Role;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDto(
        @NotBlank(message = "Username is required")
        String username,
        @NotBlank(message = "Password is required")
        String password,
        @NotBlank(message = "Repeat password is required")
        String repeatPassword,
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Surname is required")
        String surname,
        Role role
) {

    public User toUser() {
        return new User(username, password, name, surname, role);
    }
}

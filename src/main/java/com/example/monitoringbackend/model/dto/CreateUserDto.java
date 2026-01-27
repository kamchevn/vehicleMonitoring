package com.example.monitoringbackend.model.dto;

import com.example.monitoringbackend.model.User;
import com.example.monitoringbackend.model.enumerations.Role;

public record CreateUserDto(
        String username,
        String password,
        String repeatPassword,
        String name,
        String surname,
        Role role
) {

    public User toUser() {
        return new User(username, password, name, surname, role);
    }
}

package com.example.monitoringbackend.model.dto;

import com.example.monitoringbackend.model.User;
import com.example.monitoringbackend.model.enumerations.Role;

public record DisplayUserDto(
    String username, String email, String name, String surname, Role role, boolean enabled) {

  public static DisplayUserDto from(User user) {
    return new DisplayUserDto(
        user.getUsername(),
        user.getEmail(),
        user.getName(),
        user.getSurname(),
        user.getRole(),
        user.isEnabled());
  }
}

package com.example.monitoringbackend.model.dto;

import com.example.monitoringbackend.model.User;
import com.example.monitoringbackend.model.enumerations.Role;

public record DisplayUserDto(String username, String name, String surname, Role role) {

  public static DisplayUserDto from(User user) {
    return new DisplayUserDto(
        user.getUsername(), user.getName(), user.getSurname(), user.getRole());
  }
}

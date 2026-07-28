package com.example.monitoringbackend.service.domain;

import com.example.monitoringbackend.model.User;
import com.example.monitoringbackend.model.enumerations.Role;

public interface UserService {
  User getUser(String username);

  User registerUser(
      String username,
      String password,
      String repeatPassword,
      String email,
      String name,
      String surname,
      Role role);

  User confirmEmail(String token);

  User login(String username, String password);
}

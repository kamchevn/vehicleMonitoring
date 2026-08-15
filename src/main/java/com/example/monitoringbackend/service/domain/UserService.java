package com.example.monitoringbackend.service.domain;

import com.example.monitoringbackend.model.User;

public interface UserService {
  User getUser(String username);

  /** Registers a self-service account. The role is chosen by the server, never by the caller. */
  User registerUser(
      String username,
      String password,
      String repeatPassword,
      String email,
      String name,
      String surname);

  User confirmEmail(String token);

  User login(String username, String password);
}

package com.example.monitoringbackend.service.application.impl;

import com.example.monitoringbackend.helpers.JwtHelper;
import com.example.monitoringbackend.model.User;
import com.example.monitoringbackend.model.dto.CreateUserDto;
import com.example.monitoringbackend.model.dto.DisplayUserDto;
import com.example.monitoringbackend.model.dto.LoginResponseDto;
import com.example.monitoringbackend.model.dto.LoginUserDto;
import com.example.monitoringbackend.service.application.UserApplicationService;
import com.example.monitoringbackend.service.domain.UserService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationServiceImpl implements UserApplicationService {
  private final UserService userService;
  private final JwtHelper jwtHelper;

  public UserApplicationServiceImpl(UserService userService, JwtHelper jwtHelper) {
    this.userService = userService;
    this.jwtHelper = jwtHelper;
  }

  @Override
  public Optional<DisplayUserDto> register(CreateUserDto createUserDto) {
    User user =
        userService.registerUser(
            createUserDto.username(),
            createUserDto.password(),
            createUserDto.repeatPassword(),
            createUserDto.email(),
            createUserDto.name(),
            createUserDto.surname(),
            createUserDto.role());
    return Optional.of(DisplayUserDto.from(user));
  }

  @Override
  public Optional<DisplayUserDto> confirmEmail(String token) {
    return Optional.of(DisplayUserDto.from(userService.confirmEmail(token)));
  }

  @Override
  public Optional<LoginResponseDto> login(LoginUserDto loginUserDto) {
    User user = userService.login(loginUserDto.username(), loginUserDto.password());
    if (user != null) {
      String token = jwtHelper.generateToken(user);

      return Optional.of(new LoginResponseDto(token));
    }
    return Optional.empty();
  }

  @Override
  public Optional<DisplayUserDto> findByUsername(String username) {
    return Optional.of(DisplayUserDto.from(userService.getUser(username)));
  }
}

package com.example.monitoringbackend.service.application;

import com.example.monitoringbackend.model.dto.CreateUserDto;
import com.example.monitoringbackend.model.dto.DisplayUserDto;
import com.example.monitoringbackend.model.dto.LoginResponseDto;
import com.example.monitoringbackend.model.dto.LoginUserDto;

import java.util.Optional;

public interface UserApplicationService {
    Optional<DisplayUserDto> register(CreateUserDto createUserDto);
    Optional<LoginResponseDto> login(LoginUserDto loginUserDto);
    Optional<DisplayUserDto> findByUsername(String username);
}

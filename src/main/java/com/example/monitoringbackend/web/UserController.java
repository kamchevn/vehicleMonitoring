package com.example.monitoringbackend.web;

import com.example.monitoringbackend.exceptions.InvalidUserCredentialsException;
import com.example.monitoringbackend.model.dto.CreateUserDto;
import com.example.monitoringbackend.model.dto.DisplayUserDto;
import com.example.monitoringbackend.model.dto.LoginResponseDto;
import com.example.monitoringbackend.model.dto.LoginUserDto;
import com.example.monitoringbackend.service.application.UserApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@Tag(
    name = "User API",
    description = "Endpoints for user authentication and registration") // Swagger tag
public class UserController {

  private final UserApplicationService userApplicationService;

  public UserController(UserApplicationService userApplicationService) {
    this.userApplicationService = userApplicationService;
  }

  @Operation(summary = "Register a new user", description = "Creates a new user account")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or passwords do not match")
      })
  @PostMapping("/register")
  public ResponseEntity<DisplayUserDto> register(@Valid @RequestBody CreateUserDto createUserDto) {
    return userApplicationService.register(createUserDto).map(ResponseEntity::ok).orElseThrow();
  }

  @Operation(
      summary = "Confirm user email",
      description = "Enables the user account after clicking the confirmation link from email")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Email confirmed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid or expired confirmation token")
      })
  @GetMapping("/confirm")
  public ResponseEntity<DisplayUserDto> confirmEmail(@RequestParam String token) {
    return userApplicationService.confirmEmail(token).map(ResponseEntity::ok).orElseThrow();
  }

  @Operation(summary = "User login", description = "Authenticates a user and generates a JWT")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
        @ApiResponse(responseCode = "404", description = "Invalid username or password")
      })
  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginUserDto loginUserDto) {
    return userApplicationService
        .login(loginUserDto)
        .map(ResponseEntity::ok)
        .orElseThrow(InvalidUserCredentialsException::new);
  }
}

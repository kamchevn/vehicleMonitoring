package com.example.monitoringbackend.service.domain.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.example.monitoringbackend.exceptions.*;
import com.example.monitoringbackend.model.EmailConfirmationToken;
import com.example.monitoringbackend.model.User;
import com.example.monitoringbackend.model.enumerations.Role;
import com.example.monitoringbackend.repository.EmailConfirmationTokenRepository;
import com.example.monitoringbackend.repository.UserRepository;
import com.example.monitoringbackend.service.domain.EmailService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock private UserRepository userRepository;

  @Mock private EmailConfirmationTokenRepository confirmationTokenRepository;

  @Mock private PasswordEncoder passwordEncoder;

  @Mock private EmailService emailService;

  @InjectMocks private UserServiceImpl userService;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(
        userService, "confirmationBaseUrl", "http://localhost:8080/api/user/confirm");
    ReflectionTestUtils.setField(userService, "confirmationTokenExpiryHours", 24L);
  }

  @Test
  void getUser_returnsUser_whenPresent() {
    User user = new User("alice", "hashed", "alice@example.com", "Alice", "Smith", Role.ROLE_USER);
    when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));

    User result = userService.getUser("alice");

    assertEquals("alice", result.getUsername());
  }

  @Test
  void getUser_throws_whenMissing() {
    when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> userService.getUser("missing"));
  }

  @Test
  void registerUser_savesDisabledUserAndSendsConfirmationEmail() {
    when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());
    when(userRepository.findByEmail("bob@example.com")).thenReturn(Optional.empty());
    when(passwordEncoder.encode("Password1!")).thenReturn("encoded");
    when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
    when(confirmationTokenRepository.save(any(EmailConfirmationToken.class)))
        .thenAnswer(inv -> inv.getArgument(0));

    User result =
        userService.registerUser(
            "bob", "Password1!", "Password1!", "bob@example.com", "Bob", "Jones", Role.ROLE_USER);

    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(userCaptor.capture());
    User saved = userCaptor.getValue();
    assertEquals("bob", saved.getUsername());
    assertEquals("bob@example.com", saved.getEmail());
    assertEquals("encoded", saved.getPassword());
    assertFalse(saved.isEnabled());
    assertEquals("Bob", result.getName());

    ArgumentCaptor<EmailConfirmationToken> tokenCaptor =
        ArgumentCaptor.forClass(EmailConfirmationToken.class);
    verify(confirmationTokenRepository).save(tokenCaptor.capture());
    assertEquals(saved, tokenCaptor.getValue().getUser());
    assertFalse(tokenCaptor.getValue().getToken().isBlank());

    verify(emailService)
        .sendConfirmationEmail(
            eq("bob@example.com"),
            eq("bob"),
            eq(
                "http://localhost:8080/api/user/confirm?token="
                    + tokenCaptor.getValue().getToken()));
  }

  @Test
  void registerUser_throws_whenPasswordsDoNotMatch() {
    assertThrows(
        PasswordsDoNotMatchException.class,
        () ->
            userService.registerUser(
                "bob",
                "Password1!",
                "Password2!",
                "bob@example.com",
                "Bob",
                "Jones",
                Role.ROLE_USER));
    verify(userRepository, never()).save(any());
    verify(emailService, never()).sendConfirmationEmail(anyString(), anyString(), anyString());
  }

  @Test
  void registerUser_throws_whenPasswordFormatInvalid() {
    assertThrows(
        InvalidPasswordFormatException.class,
        () ->
            userService.registerUser(
                "bob", "password", "password", "bob@example.com", "Bob", "Jones", Role.ROLE_USER));
    verify(userRepository, never()).save(any());
  }

  @Test
  void registerUser_throws_whenUsernameExists() {
    when(userRepository.findByUsername("bob"))
        .thenReturn(Optional.of(new User("bob", "x", "bob@example.com", "B", "J", Role.ROLE_USER)));

    assertThrows(
        UsernameAlreadyExistsException.class,
        () ->
            userService.registerUser(
                "bob",
                "Password1!",
                "Password1!",
                "bob@example.com",
                "Bob",
                "Jones",
                Role.ROLE_USER));
    verify(userRepository, never()).save(any());
  }

  @Test
  void registerUser_throws_whenEmailExists() {
    when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());
    when(userRepository.findByEmail("bob@example.com"))
        .thenReturn(
            Optional.of(new User("other", "x", "bob@example.com", "B", "J", Role.ROLE_USER)));

    assertThrows(
        EmailAlreadyExistsException.class,
        () ->
            userService.registerUser(
                "bob",
                "Password1!",
                "Password1!",
                "bob@example.com",
                "Bob",
                "Jones",
                Role.ROLE_USER));
    verify(userRepository, never()).save(any());
  }

  @Test
  void confirmEmail_enablesUser_whenTokenValid() {
    User user = new User("alice", "encoded", "alice@example.com", "Alice", "Smith", Role.ROLE_USER);
    EmailConfirmationToken token =
        new EmailConfirmationToken("token-123", user, LocalDateTime.now().plusHours(1));
    when(confirmationTokenRepository.findByToken("token-123")).thenReturn(Optional.of(token));
    when(userRepository.save(user)).thenReturn(user);
    when(confirmationTokenRepository.save(token)).thenReturn(token);

    User result = userService.confirmEmail("token-123");

    assertTrue(result.isEnabled());
    assertTrue(token.isUsed());
    verify(userRepository).save(user);
  }

  @Test
  void confirmEmail_throws_whenTokenMissing() {
    when(confirmationTokenRepository.findByToken("missing")).thenReturn(Optional.empty());

    assertThrows(
        InvalidConfirmationTokenException.class, () -> userService.confirmEmail("missing"));
  }

  @Test
  void confirmEmail_throws_whenTokenExpired() {
    User user = new User("alice", "encoded", "alice@example.com", "Alice", "Smith", Role.ROLE_USER);
    EmailConfirmationToken token =
        new EmailConfirmationToken("token-123", user, LocalDateTime.now().minusHours(1));
    when(confirmationTokenRepository.findByToken("token-123")).thenReturn(Optional.of(token));

    assertThrows(
        InvalidConfirmationTokenException.class, () -> userService.confirmEmail("token-123"));
    assertFalse(user.isEnabled());
  }

  @Test
  void login_returnsUser_whenCredentialsValidAndEnabled() {
    User user = new User("alice", "encoded", "alice@example.com", "Alice", "Smith", Role.ROLE_USER);
    user.setEnabled(true);
    when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("Password1!", "encoded")).thenReturn(true);

    User result = userService.login("alice", "Password1!");

    assertEquals("alice", result.getUsername());
  }

  @Test
  void login_throws_whenUserMissing() {
    when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());

    assertThrows(
        InvalidUserCredentialsException.class, () -> userService.login("alice", "Password1!"));
  }

  @Test
  void login_throws_whenPasswordWrong() {
    User user = new User("alice", "encoded", "alice@example.com", "Alice", "Smith", Role.ROLE_USER);
    user.setEnabled(true);
    when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

    assertThrows(InvalidUserCredentialsException.class, () -> userService.login("alice", "wrong"));
  }

  @Test
  void login_throws_whenAccountNotEnabled() {
    User user = new User("alice", "encoded", "alice@example.com", "Alice", "Smith", Role.ROLE_USER);
    when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("Password1!", "encoded")).thenReturn(true);

    assertThrows(AccountNotEnabledException.class, () -> userService.login("alice", "Password1!"));
  }
}

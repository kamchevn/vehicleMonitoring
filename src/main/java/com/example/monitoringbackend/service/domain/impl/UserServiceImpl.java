package com.example.monitoringbackend.service.domain.impl;

import com.example.monitoringbackend.exceptions.*;
import com.example.monitoringbackend.model.EmailConfirmationToken;
import com.example.monitoringbackend.model.User;
import com.example.monitoringbackend.model.enumerations.Role;
import com.example.monitoringbackend.repository.EmailConfirmationTokenRepository;
import com.example.monitoringbackend.repository.UserRepository;
import com.example.monitoringbackend.service.domain.EmailService;
import com.example.monitoringbackend.service.domain.UserService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final EmailConfirmationTokenRepository confirmationTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;

  @Value("${app.confirmation-base-url}")
  private String confirmationBaseUrl;

  @Value("${app.confirmation-token-expiry-hours:24}")
  private long confirmationTokenExpiryHours;

  public UserServiceImpl(
      UserRepository userRepository,
      EmailConfirmationTokenRepository confirmationTokenRepository,
      PasswordEncoder passwordEncoder,
      EmailService emailService) {
    this.userRepository = userRepository;
    this.confirmationTokenRepository = confirmationTokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
  }

  @Override
  public User getUser(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException(username));
  }

  @Override
  @Transactional
  public User registerUser(
      String username,
      String password,
      String repeatPassword,
      String email,
      String name,
      String surname,
      Role role) {
    if (!password.equals(repeatPassword)) {
      throw new PasswordsDoNotMatchException();
    }

    if (!password.matches(".*[A-Z].*")
        || !password.matches(".*\\d.*")
        || !password.matches(".*[^A-Za-z0-9].*")) {

      throw new InvalidPasswordFormatException();
    }

    if (this.userRepository.findByUsername(username).isPresent()) {
      throw new UsernameAlreadyExistsException(username);
    }

    if (this.userRepository.findByEmail(email).isPresent()) {
      throw new EmailAlreadyExistsException(email);
    }

    User user = new User(username, passwordEncoder.encode(password), email, name, surname, role);
    user.setEnabled(false);
    User savedUser = userRepository.save(user);

    String tokenValue = UUID.randomUUID().toString();
    EmailConfirmationToken confirmationToken =
        new EmailConfirmationToken(
            tokenValue, savedUser, LocalDateTime.now().plusHours(confirmationTokenExpiryHours));
    confirmationTokenRepository.save(confirmationToken);

    String confirmationLink = confirmationBaseUrl + "?token=" + tokenValue;
    emailService.sendConfirmationEmail(
        savedUser.getEmail(), savedUser.getUsername(), confirmationLink);

    return savedUser;
  }

  @Override
  @Transactional
  public User confirmEmail(String token) {
    EmailConfirmationToken confirmationToken =
        confirmationTokenRepository
            .findByToken(token)
            .orElseThrow(InvalidConfirmationTokenException::new);

    if (confirmationToken.isUsed() || confirmationToken.isExpired()) {
      throw new InvalidConfirmationTokenException();
    }

    User user = confirmationToken.getUser();
    user.setEnabled(true);
    confirmationToken.setUsed(true);

    userRepository.save(user);
    confirmationTokenRepository.save(confirmationToken);
    return user;
  }

  @Override
  public User login(String username, String password) throws InvalidUserCredentialsException {
    User user =
        userRepository.findByUsername(username).orElseThrow(InvalidUserCredentialsException::new);
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new InvalidUserCredentialsException();
    }
    if (!user.isEnabled()) {
      throw new AccountNotEnabledException();
    }
    return user;
  }
}

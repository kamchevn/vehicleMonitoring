package com.example.monitoringbackend.service.domain.impl;

import com.example.monitoringbackend.exceptions.*;
import com.example.monitoringbackend.model.User;
import com.example.monitoringbackend.model.enumerations.Role;
import com.example.monitoringbackend.repository.UserRepository;
import com.example.monitoringbackend.service.domain.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public User registerUser(String username, String password, String repeatPassword, String name, String surname, Role role) {
        if (!password.equals(repeatPassword)) {
            throw new PasswordsDoNotMatchException();
        }

        if (!password.matches(".*[A-Z].*") ||
                !password.matches(".*\\d.*") ||
                !password.matches(".*[^A-Za-z0-9].*")) {

            throw new InvalidPasswordFormatException();
        }

        if (this.userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException(username);
        }

        User user = new User(username, passwordEncoder.encode(password), name, surname, role);

        return userRepository.save(user);
    }

    @Override
    public User login(String username, String password) throws InvalidUserCredentialsException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidUserCredentialsException::new);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new InvalidUserCredentialsException();
        return user;
    }
}

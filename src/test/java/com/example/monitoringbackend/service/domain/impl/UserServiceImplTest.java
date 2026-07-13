package com.example.monitoringbackend.service.domain.impl;

import com.example.monitoringbackend.exceptions.*;
import com.example.monitoringbackend.model.User;
import com.example.monitoringbackend.model.enumerations.Role;
import com.example.monitoringbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUser_returnsUser_whenPresent() {
        User user = new User("alice", "hashed", "Alice", "Smith", Role.ROLE_USER);
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
    void registerUser_savesEncodedPassword_whenValid() {
        when(userRepository.findByUsername("bob")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Password1!")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        User result = userService.registerUser(
                "bob", "Password1!", "Password1!", "Bob", "Jones", Role.ROLE_USER
        );

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("bob", captor.getValue().getUsername());
        assertEquals("encoded", captor.getValue().getPassword());
        assertEquals("Bob", result.getName());
    }

    @Test
    void registerUser_throws_whenPasswordsDoNotMatch() {
        assertThrows(PasswordsDoNotMatchException.class, () ->
                userService.registerUser("bob", "Password1!", "Password2!", "Bob", "Jones", Role.ROLE_USER)
        );
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_throws_whenPasswordFormatInvalid() {
        assertThrows(InvalidPasswordFormatException.class, () ->
                userService.registerUser("bob", "password", "password", "Bob", "Jones", Role.ROLE_USER)
        );
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_throws_whenUsernameExists() {
        when(userRepository.findByUsername("bob"))
                .thenReturn(Optional.of(new User("bob", "x", "B", "J", Role.ROLE_USER)));

        assertThrows(UsernameAlreadyExistsException.class, () ->
                userService.registerUser("bob", "Password1!", "Password1!", "Bob", "Jones", Role.ROLE_USER)
        );
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_returnsUser_whenCredentialsValid() {
        User user = new User("alice", "encoded", "Alice", "Smith", Role.ROLE_USER);
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Password1!", "encoded")).thenReturn(true);

        User result = userService.login("alice", "Password1!");

        assertEquals("alice", result.getUsername());
    }

    @Test
    void login_throws_whenUserMissing() {
        when(userRepository.findByUsername("alice")).thenReturn(Optional.empty());

        assertThrows(InvalidUserCredentialsException.class, () -> userService.login("alice", "Password1!"));
    }

    @Test
    void login_throws_whenPasswordWrong() {
        User user = new User("alice", "encoded", "Alice", "Smith", Role.ROLE_USER);
        when(userRepository.findByUsername("alice")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(InvalidUserCredentialsException.class, () -> userService.login("alice", "wrong"));
    }
}

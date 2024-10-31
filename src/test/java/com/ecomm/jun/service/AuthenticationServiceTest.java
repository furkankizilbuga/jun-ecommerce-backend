package com.ecomm.jun.service;

import com.ecomm.jun.dto.UserRequest;
import com.ecomm.jun.entity.Authority;
import com.ecomm.jun.entity.Role;
import com.ecomm.jun.entity.User;
import com.ecomm.jun.exceptions.UserException;
import com.ecomm.jun.repository.AuthorityRepository;
import com.ecomm.jun.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        userRequest = new UserRequest();
        userRequest.setPassword("12345678");
        userRequest.setEmail("test@example.com");
    }

    @Test
    void register() {
        // Arrange
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");
        when(authorityRepository.findByAuthority(Role.USER)).thenReturn(Optional.empty());

        Authority userRole = new Authority();
        userRole.setAuthority(Role.USER);
        when(authorityRepository.save(any(Authority.class))).thenReturn(userRole);

        // Act
        authenticationService.register(userRequest);

        // Assert
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_already_exists() {
        // Arrange
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(new User()));

        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> authenticationService.register(userRequest));
        assertEquals("User with given email already exists!", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    void registerAdmin() {
        // Arrange
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRequest.getPassword())).thenReturn("encodedPassword");
        when(authorityRepository.findByAuthority(Role.ADMIN)).thenReturn(Optional.empty());

        Authority adminRole = new Authority();
        adminRole.setAuthority(Role.ADMIN);
        when(authorityRepository.save(any(Authority.class))).thenReturn(adminRole);

        // Act
        authenticationService.registerAdmin(userRequest);

        // Assert
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerAdmin_already_exists() {
        // Arrange
        when(userRepository.findByEmail(userRequest.getEmail())).thenReturn(Optional.of(new User()));

        // Act & Assert
        UserException exception = assertThrows(UserException.class, () -> authenticationService.registerAdmin(userRequest));
        assertEquals("User with given email already exists!", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }
}

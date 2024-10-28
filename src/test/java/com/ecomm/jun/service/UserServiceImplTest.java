package com.ecomm.jun.service;

import com.ecomm.jun.entity.Product;
import com.ecomm.jun.entity.User;
import com.ecomm.jun.exceptions.UserException;
import com.ecomm.jun.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {

        user = new User();
        user.setId(1L);
        user.setPassword("1234567");
        user.setEmail("mail@test.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setCreatedAt(LocalDateTime.now());

    }

    @Test
    void findAll() {
        userService.findAll();
        verify(userRepository).findAll();
    }

    @Test
    void findById() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        User found = userService.findById(1L);

        verify(userRepository).findById(1L);
        assertEquals(user.getId(), found.getId());
        assertEquals(user.getEmail(), found.getEmail());
    }

    @Test
    void findById_not_found() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> userService.findById(1L));

        verify(userRepository).findById(1L);
        assertEquals("User with given ID could not be found!", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void findByEmail() {
        when(userRepository.findByEmail("mail@test.com")).thenReturn(java.util.Optional.of(user));

        User found = userService.findByEmail("mail@test.com");

        verify(userRepository).findByEmail("mail@test.com");
        assertEquals(user.getId(), found.getId());
        assertEquals(user.getFirstName(), found.getFirstName());
    }

    @Test
    void findByEmail_not_found() {
        when(userRepository.findByEmail("mail@test.com")).thenReturn(java.util.Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> userService.findByEmail("mail@test.com"));

        verify(userRepository).findByEmail("mail@test.com");
        assertEquals("User with given email could not be found!", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void findUserProducts() {
        //Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        List<Product> products = new ArrayList<>();
        products.add(product);
        user.setProducts(products);

        when(userRepository.findUserProducts(1L)).thenReturn(user.getProducts());

        //Act
        List<Product> foundProducts = userService.findUserProducts(1L);

        //Assert
        verify(userRepository).findUserProducts(1L);
        assertEquals(user.getProducts().get(0).getId(), foundProducts.get(0).getId());
    }

    @Test
    void save() {
        when(userRepository.save(user)).thenReturn(user);

        User saved = userService.save(user);

        verify(userRepository).save(user);
        assertEquals(user.getId(), saved.getId());
        assertEquals(user.getEmail(), saved.getEmail());
    }

    @Test
    void delete() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));

        userService.delete(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void delete_not_found() {
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> userService.delete(1L));


        verify(userRepository, never()).deleteById(1L);
        assertEquals("User with given ID could not be found!", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void getAuthenticatedEmail() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        when(authentication.getName()).thenReturn("mail@test.com");


        String email = userService.getAuthenticatedEmail();

        assertEquals("mail@test.com", email);
        verify(securityContext).getAuthentication();
        verify(authentication).getName();

        SecurityContextHolder.clearContext();
    }

}
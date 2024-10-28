package com.ecomm.jun.controller;

import com.ecomm.jun.dto.UserDto;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.entity.User;
import com.ecomm.jun.exceptions.UserException;
import com.ecomm.jun.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

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
    void findAll() throws Exception {
        List<User> users = List.of(user);
        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(user.getId()))
                .andExpect(jsonPath("$[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$").isArray());

        verify(userService).findAll();
    }

    @Test
    void findById() throws Exception {
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("mail@test.com"));

        verify(userService).findById(1L);
    }

    @Test
    void findById_not_found() throws Exception {
        when(userService.findById(99L)).thenThrow(new UserException("User with given ID not found!", HttpStatus.BAD_REQUEST));

        mockMvc.perform(get("/user/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User with given ID not found!"));

        verify(userService).findById(99L);
    }

    @Test
    void findByEmail() throws Exception {
        when(userService.findByEmail("mail@test.com")).thenReturn(user);

        mockMvc.perform(get("/user/email")
                        .param("email", "mail@test.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()));

        verify(userService).findByEmail("mail@test.com");
    }

    @Test
    void findByEmail_not_found() throws Exception {
        when(userService.findByEmail("no@test.com")).thenThrow(new UserException("User with given email is not found!", HttpStatus.BAD_REQUEST));

        mockMvc.perform(get("/user/email")
                        .param("email", "no@test.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User with given email is not found!"));

        verify(userService).findByEmail("no@test.com");
    }

    @Test
    void getAuthenticatedEmail() {
    }

    @Test
    void findUserProduct() throws Exception {
        //Arrange
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        List<Product> products = new ArrayList<>();
        products.add(product);
        user.setProducts(products);

        when(userService.findUserProducts(1L)).thenReturn(products);

        //Act & Assert
        mockMvc.perform(get("/user/product")
                        .param("userId", user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(product.getId()))
                .andExpect(jsonPath("$[0].name").value(product.getName()));

        verify(userService).findUserProducts(user.getId());
    }

    @Test
    void saveUser() throws Exception {
        when(userService.save(user)).thenReturn(user);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

        verify(userService).save(user);
    }

    @Test
    void deleteUser() throws Exception {
        when(userService.delete(1L)).thenReturn(user);

        mockMvc.perform(delete("/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

        verify(userService).delete(1L);
    }

    private static String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return dateTime.format(formatter);
    }

    public static String jsonToString(User user) {
        return "{"
                + "\"id\":" + user.getId() + ","
                + "\"email\":\"" + user.getEmail() + "\","
                + "\"firstName\":\"" + user.getFirstName() + "\","
                + "\"lastName\":\"" + user.getLastName() + "\","
                + "\"createdAt\":\"" + formatDate(user.getCreatedAt()) + "\""
                + "}";
    }
}


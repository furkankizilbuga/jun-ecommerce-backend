package com.ecomm.jun.controller;

import com.ecomm.jun.dto.UserRequest;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.entity.User;
import com.ecomm.jun.repository.ProductRepository;
import com.ecomm.jun.repository.UserRepository;
import com.ecomm.jun.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private User user;

    @BeforeEach
    void setup() {
        //Arrange
        userRepository.deleteAll();
        productRepository.deleteAll();

        user = new User();
        user.setId(1L);
        user.setPassword("1234567");
        user.setEmail("mail@test.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setCreatedAt(LocalDateTime.now());

        user = userRepository.save(user);

    }

    @AfterEach
    void cleanup() {
        userRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    void findAll() throws Exception {

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(user.getId()))
                .andExpect(jsonPath("$[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$").isArray());

    }

    @Test
    void findById() throws Exception {

        mockMvc.perform(get("/user/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

    }

    @Test
    void findById_not_found() throws Exception {

        mockMvc.perform(get("/user/{id}", 99L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with given ID could not be found!"));

    }

    @Test
    void findByEmail() throws Exception {

        mockMvc.perform(get("/user/email")
                        .param("email", "mail@test.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()));

    }

    @Test
    void findByEmail_not_found() throws Exception {

        mockMvc.perform(get("/user/email")
                        .param("email", "no@test.com")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with given email could not be found!"));

    }


    @Test
    void findUserProduct() throws Exception {
        //Arrange
        Product product = new Product();
        product.setName("Test Product");
        productRepository.save(product);

        List<Product> products = new ArrayList<>();
        products.add(product);
        user.setProducts(products);
        user = userRepository.save(user);


        //Act & Assert
        mockMvc.perform(get("/user/product")
                        .param("userId", user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(product.getId()))
                .andExpect(jsonPath("$[0].name").value(product.getName()));

    }

    @Test
    void saveUser() throws Exception {
        //Arrange
        UserRequest userRequest = new UserRequest();
        userRequest.setPassword("different");
        userRequest.setEmail("different@test.com");
        userRequest.setFirstName("Test");
        userRequest.setLastName("User");

        //Act & Assert
        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(userRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value(userRequest.getFirstName()))
                .andExpect(jsonPath("$.email").value(userRequest.getEmail()));

    }

    @Test
    void deleteUser() throws Exception {

        mockMvc.perform(delete("/user/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

    }


    public static String jsonToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}


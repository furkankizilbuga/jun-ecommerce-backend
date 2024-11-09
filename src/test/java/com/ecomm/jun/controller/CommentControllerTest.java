package com.ecomm.jun.controller;

import com.ecomm.jun.dto.CategoryRequest;
import com.ecomm.jun.dto.CommentRequest;
import com.ecomm.jun.dto.ProductRequest;
import com.ecomm.jun.entity.Category;
import com.ecomm.jun.entity.Comment;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.entity.User;
import com.ecomm.jun.exceptions.CommentException;
import com.ecomm.jun.repository.CategoryRepository;
import com.ecomm.jun.repository.CommentRepository;
import com.ecomm.jun.repository.ProductRepository;
import com.ecomm.jun.repository.UserRepository;
import com.ecomm.jun.service.CategoryService;
import com.ecomm.jun.service.CommentService;
import com.ecomm.jun.service.ProductService;
import com.ecomm.jun.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private CommentService commentService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;


    //Repositories to deleteAll before and after each test.
    //Did not implement this in service because it should never be allowed.
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;


    private Comment comment1;
    private Comment comment2;
    private Product product;
    private User user;

    @BeforeEach
    void setup() {
        //Arrange
        commentRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        user = new User();
        user.setEmail("test@test.com");
        user.setPassword("password123");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setCreatedAt(LocalDateTime.now());
        userService.save(user);

        CategoryRequest categoryRequest = new CategoryRequest("Test Category");
        Category category = categoryService.save(categoryRequest);

        ProductRequest productRequest = new ProductRequest("Test Product", "", 15.0, 5.0, Set.of(category.getId()));
        product = productService.save(productRequest);

        CommentRequest commentRequest1 = new CommentRequest("TEST CONTENT");
        CommentRequest commentRequest2 = new CommentRequest("TEST CONTENT2");

        comment1 = commentService.save(commentRequest1, product.getId());
        comment2 = commentService.save(commentRequest2, product.getId());

    }

    @AfterEach
    void cleanup() {
        commentRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void findAll() throws Exception {
        mockMvc.perform(get("/comment"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(comment1.getId()))
                .andExpect(jsonPath("$[0].content").value(comment1.getContent()))
                .andExpect(jsonPath("$[1].id").value(comment2.getId()))
                .andExpect(jsonPath("$[1].content").value(comment2.getContent()));
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void findById() throws Exception {

        mockMvc.perform(get("/comment/{id}", comment1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comment1.getId()))
                .andExpect(jsonPath("$.content").value(comment1.getContent()));

    }

    @Test
    @WithMockUser(username = "test@test.com")
    void findById_not_found() throws Exception {

        mockMvc.perform(get("/comment/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("A comment with given ID could not be found"));

    }

    @Test
    @WithMockUser(username = "test@test.com")
    void findByUserId() throws Exception {

        mockMvc.perform(get("/comment/user/{userId}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(comment1.getId()))
                .andExpect(jsonPath("$.[0].content").value(comment1.getContent()))
                .andExpect(jsonPath("$.[1].id").value(comment2.getId()))
                .andExpect(jsonPath("$.[1].content").value(comment2.getContent()));

    }

    @Test
    @WithMockUser(username = "test@test.com")
    void findByProductId() throws Exception {

        mockMvc.perform(get("/comment/product/{productId}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(comment1.getId()))
                .andExpect(jsonPath("$.[0].content").value(comment1.getContent()))
                .andExpect(jsonPath("$.[1].id").value(comment2.getId()))
                .andExpect(jsonPath("$.[1].content").value(comment2.getContent()));

    }

    @Test
    @WithMockUser(username = "test@test.com")
    void save() throws Exception {
        CommentRequest commentRequest3 = new CommentRequest("TEST TEXT3");

        mockMvc.perform(post("/comment/{id}", product.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonToString(commentRequest3)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(commentRequest3.content()));
    }

    @Test
    @WithMockUser(username = "test@test.com")
    void save_bad_request() throws Exception {
        CommentRequest request = new CommentRequest("");

        mockMvc.perform(post("/comment/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Please write your comment before you submit!"));
    }

    public static String jsonToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
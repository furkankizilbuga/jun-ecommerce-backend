package com.ecomm.jun.controller;

import com.ecomm.jun.dto.CommentRequest;
import com.ecomm.jun.entity.Comment;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.entity.User;
import com.ecomm.jun.exceptions.CommentException;
import com.ecomm.jun.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    private Comment comment1;
    private Comment comment2;
    private List<Comment> commentList;
    private Product product;
    private User user;

    @BeforeEach
    void setUp() {

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        comment1 = new Comment();
        comment1.setId(1L);
        comment1.setContent("TEST TEXT");
        comment1.setCreatedAt(LocalDate.now());
        comment1.setProduct(product);
        comment1.setUser(user);

        comment2 = new Comment();
        comment2.setId(2L);
        comment2.setContent("TEST TEXT2");
        comment2.setCreatedAt(LocalDate.now());
        comment2.setProduct(product);
        comment2.setUser(user);

        commentList = new ArrayList<>();
        commentList.add(comment1);
        commentList.add(comment2);


    }

    @Test
    void findAll() throws Exception {
        when(commentService.findAll()).thenReturn(commentList);

        mockMvc.perform(get("/comment"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(commentService).findAll();
    }

    @Test
    void findById() throws Exception {
        when(commentService.findById(comment1.getId())).thenReturn(comment1);

        mockMvc.perform(get("/comment/{id}", comment1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comment1.getId()))
                .andExpect(jsonPath("$.content").value(comment1.getContent()));

        verify(commentService).findById(comment1.getId());
    }

    @Test
    void findById_not_found() throws Exception {
        when(commentService.findById(99L)).thenThrow(new CommentException("Comment with given ID is not found!", HttpStatus.BAD_REQUEST));

        mockMvc.perform(get("/comment/{id}", 99L))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Comment with given ID is not found!"));

        verify(commentService).findById(99L);
    }

    @Test
    void findByUserId() throws Exception {
        when(commentService.findByUserId(user.getId())).thenReturn(commentList);

        mockMvc.perform(get("/comment/user/{userId}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(comment1.getId()))
                .andExpect(jsonPath("$.[0].content").value(comment1.getContent()))
                .andExpect(jsonPath("$.[1].id").value(comment2.getId()))
                .andExpect(jsonPath("$.[1].content").value(comment2.getContent()));

        verify(commentService).findByUserId(user.getId());
    }

    @Test
    void findByProductId() throws Exception {
        when(commentService.findByProductId(product.getId())).thenReturn(commentList);

        mockMvc.perform(get("/comment/product/{productId}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(comment1.getId()))
                .andExpect(jsonPath("$.[0].content").value(comment1.getContent()))
                .andExpect(jsonPath("$.[1].id").value(comment2.getId()))
                .andExpect(jsonPath("$.[1].content").value(comment2.getContent()));

        verify(commentService).findByProductId(product.getId());
    }

    @Test
    void save() throws Exception {
        CommentRequest request = new CommentRequest("TEST TEXT");
        when(commentService.save(request, product.getId())).thenReturn(comment1);

        mockMvc.perform(post("/comment/{id}", product.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonToString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(comment1.getId()))
                .andExpect(jsonPath("$.content").value("TEST TEXT"));
    }

    @Test
    void save_bad_request() throws Exception {
        CommentRequest request = new CommentRequest("");
        when(commentService.save(request, product.getId())).thenThrow(new CommentException("Bad Request!", HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/comment/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Bad Request!"));
    }

    public static String jsonToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
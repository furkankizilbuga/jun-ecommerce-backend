package com.ecomm.jun.service;

import com.ecomm.jun.dto.CommentRequest;
import com.ecomm.jun.entity.Comment;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.entity.User;
import com.ecomm.jun.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ProductService productService;

    @Mock
    private UserService userService;

    private Comment comment;
    private Product product;
    private User user;
    private List<Comment> commentList;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        comment = new Comment();
        comment.setId(1L);
        comment.setContent("TEST TEXT");
        comment.setCreatedAt(LocalDate.now());
        comment.setProduct(product);
        comment.setUser(user);

        commentList = new ArrayList<>();
        commentList.add(comment);

    }

    @Test
    void findAll() {
        commentService.findAll();
        verify(commentRepository).findAll();
    }

    @Test
    void findById() {
        when(commentRepository.findById(1L)).thenReturn(java.util.Optional.of(comment));

        Comment found = commentService.findById(1L);

        verify(commentRepository).findById(1L);
        assertNotNull(found);
        assertEquals(comment.getId(), found.getId());
        assertEquals(comment.getContent(), found.getContent());
    }

    @Test
    void findByUserId() {
        when(commentRepository.findByUserId(user.getId())).thenReturn(commentList);

        List<Comment> foundList = commentService.findByUserId(user.getId());

        verify(commentRepository).findByUserId(user.getId());
        assertNotNull(foundList);
        assertEquals(comment.getId(), foundList.get(0).getId());
        assertEquals(comment.getContent(), foundList.get(0).getContent());
    }

    @Test
    void findByProductId() {
        when(commentRepository.findByProductId(product.getId())).thenReturn(commentList);

        List<Comment> foundList = commentService.findByProductId(product.getId());

        verify(commentRepository).findByProductId(product.getId());
        assertEquals(comment.getId(), foundList.get(0).getId());
        assertEquals(comment.getContent(), foundList.get(0).getContent());
    }

    @Test
    void save() {
        //Arrange
        CommentRequest request = new CommentRequest("TEST TEXT");

        when(productService.findById(1L)).thenReturn(product);
        when(userService.getAuthenticatedEmail()).thenReturn("test@example.com");
        when(userService.findByEmail("test@example.com")).thenReturn(user);

        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment savedComment = invocation.getArgument(0);
            savedComment.setId(1L);
            return savedComment;
        });

        //Act
        Comment saved = commentService.save(request, 1L);

        //Assert
        verify(productService).findById(1L);
        verify(userService).getAuthenticatedEmail();
        verify(userService).findByEmail("test@example.com");
        verify(commentRepository).save(any(Comment.class));

        assertNotNull(saved);
        assertEquals("TEST TEXT", saved.getContent());
        assertNotNull(saved.getProduct());
        assertEquals(product.getId(), saved.getProduct().getId());
        assertEquals(product.getName(), saved.getProduct().getName());
        assertEquals(user, saved.getUser());
    }

    @Test
    void delete() {
        when(commentRepository.findById(1L)).thenReturn(java.util.Optional.of(comment));

        Comment deleted = commentService.delete(1L);

        verify(commentRepository).findById(1L);
        verify(commentRepository).delete(comment);
        assertEquals(comment, deleted);
    }
}
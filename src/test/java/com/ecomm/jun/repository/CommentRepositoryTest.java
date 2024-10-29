package com.ecomm.jun.repository;

import com.ecomm.jun.entity.Comment;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private User user;
    private Product product;
    private List<Comment> commentList;

    @BeforeEach
    void setup() {

        //Arrange
        user = new User();
        user.setPassword("1234567");
        user.setEmail("mail@test.com");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        product = new Product();
        product.setName("Test Product");
        productRepository.save(product);

        Comment comment1 = new Comment();
        comment1.setContent("TEST TEXT");
        comment1.setUser(user);
        comment1.setProduct(product);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setContent("TEST TEXT2");
        comment2.setUser(user);
        comment2.setProduct(product);
        commentRepository.save(comment2);

        commentList = new ArrayList<>();
        commentList.add(comment1);
        commentList.add(comment2);

    }

    @Test
    void findByUserId() {
        //Act
        List<Comment> userComments = commentRepository.findByUserId(user.getId());

        //Assert
        assertNotNull(userComments);
        assertNotNull(user.getId());
        assertFalse(userComments.isEmpty());
        assertEquals(2, userComments.size());
        assertEquals(userComments.get(0).getUser().getId(), user.getId());
        assertEquals(userComments.get(1).getUser().getId(), user.getId());
    }

    @Test
    void findByProductId() {
        List<Comment> productComments = commentRepository.findByProductId(product.getId());

        assertEquals(productComments, commentList);
        assertEquals(productComments.size(), commentList.size());
        assertEquals(productComments.get(0).getId(), commentList.get(0).getId());
        assertEquals(productComments.get(1).getId(), commentList.get(1).getId());
    }

}
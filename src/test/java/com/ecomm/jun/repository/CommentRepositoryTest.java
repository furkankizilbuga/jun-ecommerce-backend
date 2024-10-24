package com.ecomm.jun.repository;

import com.ecomm.jun.entity.Comment;
import com.ecomm.jun.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setup() {

        //Arrange
        user = new User();
        user.setPassword("1234567");
        user.setEmail("mail@test.com");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        Comment comment1 = new Comment();
        comment1.setContent("TEST TEXT");
        comment1.setUser(user);
        commentRepository.save(comment1);

        Comment comment2 = new Comment();
        comment2.setContent("TEST TEXT2");
        comment2.setUser(user);
        commentRepository.save(comment2);

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

}
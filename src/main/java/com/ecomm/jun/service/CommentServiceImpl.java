package com.ecomm.jun.service;

import com.ecomm.jun.entity.Comment;
import com.ecomm.jun.entity.User;
import com.ecomm.jun.exceptions.CommentException;
import com.ecomm.jun.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private UserService userService;

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() ->
                    new CommentException("A comment with given ID could not be found", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public Comment save(Comment comment) {
        String loggedEmail = userService.getAuthenticatedEmail();
        User user = userService.findByEmail(loggedEmail);
        comment.setUser(user);
        comment.setCreatedAt(LocalDate.now());
        return commentRepository.save(comment);
    }

    @Override
    public Comment delete(Long id) {
        Comment toBeDeleted = findById(id);
        commentRepository.delete(toBeDeleted);
        return toBeDeleted;
    }
}

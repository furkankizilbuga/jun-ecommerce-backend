package com.ecomm.jun.service;

import com.ecomm.jun.dto.CommentRequest;
import com.ecomm.jun.entity.Comment;
import com.ecomm.jun.entity.Product;
import com.ecomm.jun.entity.Role;
import com.ecomm.jun.entity.User;
import com.ecomm.jun.exceptions.CommentException;
import com.ecomm.jun.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private ProductService productService;
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
    public List<Comment> findByUserId(Long userId) {
        return commentRepository.findByUserId(userId);
    }

    @Override
    public List<Comment> findByProductId(Long productId) {
        return commentRepository.findByProductId(productId);
    }

    @Transactional
    @Override
    public Comment save(CommentRequest request, Long productId) {

        if(request.content().isEmpty()) {
            throw new CommentException("Please write your comment before you submit!", HttpStatus.BAD_REQUEST);
        }

        Product product = productService.findById(productId);

        String loggedEmail = userService.getAuthenticatedEmail();
        User user = userService.findByEmail(loggedEmail);

        Comment comment = new Comment();
        comment.setProduct(product);
        comment.setContent(request.content());
        comment.setCreatedAt(LocalDate.now());
        comment.setUser(user);

        return commentRepository.save(comment);
    }

    @Transactional
    @Override
    public Comment delete(Long id) {
        Comment toBeDeleted = findById(id);
        commentRepository.delete(toBeDeleted);
        return toBeDeleted;
    }

}

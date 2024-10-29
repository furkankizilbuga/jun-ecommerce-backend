package com.ecomm.jun.controller;

import com.ecomm.jun.dto.CommentRequest;
import com.ecomm.jun.entity.Comment;
import com.ecomm.jun.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;

    @GetMapping
    public List<Comment> findAll() {
        return commentService.findAll();
    }

    @GetMapping("/{id}")
    public Comment findById(@PathVariable Long id) {
        return commentService.findById(id);
    }

    @GetMapping("/user/{userId}")
    public List<Comment> findByUserId(@PathVariable Long userId) {
        return commentService.findByUserId(userId);
    }

    @GetMapping("/product/{productId}")
    public List<Comment> findByProductId(@PathVariable Long productId) {
        return commentService.findByProductId(productId);
    }

    @PostMapping("/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment save(@RequestBody CommentRequest request, @PathVariable Long productId) {
        return commentService.save(request, productId);
    }

}

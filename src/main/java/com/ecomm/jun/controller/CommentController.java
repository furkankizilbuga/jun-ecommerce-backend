package com.ecomm.jun.controller;

import com.ecomm.jun.entity.Comment;
import com.ecomm.jun.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{productId}")
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

    @PostMapping
    public Comment save(@RequestBody Comment comment) {

    }

}

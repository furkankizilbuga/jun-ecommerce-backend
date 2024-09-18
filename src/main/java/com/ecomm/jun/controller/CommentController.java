package com.ecomm.jun.controller;

import com.ecomm.jun.dto.CommentRequest;
import com.ecomm.jun.entity.Comment;
import com.ecomm.jun.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
@AllArgsConstructor
public class CommentController {

    private CommentService commentService;

    @GetMapping("/comments/{productId}")
    public List<Comment> findAll() {
        return commentService.findAll();
    }

    @GetMapping("/comments/{productId}/{id}")
    public Comment findById(@PathVariable Long id) {
        return commentService.findById(id);
    }

    @PostMapping("/{productId}")
    public Comment save(@RequestBody CommentRequest request, @PathVariable Long productId) {
        return commentService.save(request, productId);
    }

}

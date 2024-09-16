package com.ecomm.jun.service;

import com.ecomm.jun.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> findAll();
    Comment findById(Long id);
    //TODO -  User kısmına taşınacak | List<Comment> findByUserId(Long userId);
    Comment save(Comment comment);
    Comment delete(Long id);

}

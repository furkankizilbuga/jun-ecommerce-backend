package com.ecomm.jun.repository;

import com.ecomm.jun.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.user.id = :userId")
    List<Comment> findByUserId(Long userId);
}

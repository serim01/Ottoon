package com.sparta.ottoon.comment.repository;

import com.sparta.ottoon.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository <Comment, Long>{
    List<Comment> findByPostId(Long postId);
    Optional<Comment> findByIdAndPostIdAndUserId(Long commentId,Long postId,Long userId);

}

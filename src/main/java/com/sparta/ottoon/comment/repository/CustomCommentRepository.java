package com.sparta.ottoon.comment.repository;

import com.sparta.ottoon.comment.dto.CommentResponseDto;

import java.util.Optional;

public interface CustomCommentRepository {
    Optional<CommentResponseDto> findWithLikeCountById(Long id);
}

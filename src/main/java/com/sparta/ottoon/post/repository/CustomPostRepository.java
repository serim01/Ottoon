package com.sparta.ottoon.post.repository;

import com.sparta.ottoon.post.dto.PostResponseDto;

import java.util.Optional;

public interface CustomPostRepository {
    Optional<PostResponseDto> findWithLikeCountById(Long id);
}

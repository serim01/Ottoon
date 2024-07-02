package com.sparta.ottoon.like.repository;

import com.sparta.ottoon.comment.dto.CommentResponseDto;
import com.sparta.ottoon.post.dto.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LikeRepositoryCustom {
    Page<PostResponseDto> findLikedPostsByUserId(Long id, Pageable pageable);
    Page<CommentResponseDto> findLikedCommentsByUserId(Long id, Pageable pageable);
}

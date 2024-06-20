package com.sparta.ottoon.comment.service;

import com.sparta.ottoon.auth.jwt.JwtUtil;
import com.sparta.ottoon.auth.repository.UserRepository;
import com.sparta.ottoon.comment.dto.CommentResponseDto;
import com.sparta.ottoon.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private JwtUtil jwtUtil;

    public CommentResponseDto createComment(Long postId) {
    }
    public CommentResponseDto getComment(Long commentId) {
    }
}

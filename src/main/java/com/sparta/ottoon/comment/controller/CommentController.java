package com.sparta.ottoon.comment.controller;

import com.sparta.ottoon.comment.dto.CommentResponseDto;
import com.sparta.ottoon.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post/{postId}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //comment 생성
    @PostMapping("/comment")
    public CommentResponseDto createComment(@PathVariable Long postId){
        return commentService.createComment(postId);
    }

    //comment 조회
    @GetMapping("/comment")
    public CommentResponseDto getComment(@PathVariable Long postId){
        return commentService.getComment(postId);
    }



}

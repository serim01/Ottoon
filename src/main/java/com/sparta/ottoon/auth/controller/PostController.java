package com.sparta.ottoon.auth.controller;

import com.sparta.ottoon.auth.dto.PostRequestDto;
import com.sparta.ottoon.auth.dto.PostResponseDto;
import com.sparta.ottoon.auth.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // 게시글 등록
    @PostMapping("/posts")
    public ResponseEntity<PostResponseDto> create(@Valid @RequestBody PostRequestDto postRequestDto) {
        PostResponseDto post = postService.save(postRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    // 게시글 전체 조회
    @GetMapping("/posts")
    public ResponseEntity<Map<String, Object>> getAll() {
        List<PostResponseDto> postContents = postService.getAll();
        if (postContents.isEmpty()) {
            // 게시글이 비어있는 경우
            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", HttpStatus.OK.value());
            response.put("message", "먼저 게시글을 작성하세요.");
            return ResponseEntity.ok().body(response);
        } else {
            // 게시글이 있는 경우
            return ResponseEntity.ok().body(Collections.singletonMap("postContents", postContents));
        }
    }

    // 게시글 부분 조회
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> findById(@PathVariable(name = "postId") long postId) {
        return ResponseEntity.ok().body(postService.findById(postId));
    }

    // 게시글 수정
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> update(@PathVariable(name = "postId") long postId, @Valid @RequestBody PostRequestDto postRequestDto) {
        return ResponseEntity.ok().body(postService.update(postId, postRequestDto));
    }

    //게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable(name = "postId") long postId) {
        postService.delete(postId);
        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.OK.value());
        response.put("message", "게시글 삭제 완료");
        return ResponseEntity.ok().body(response);
    }
}

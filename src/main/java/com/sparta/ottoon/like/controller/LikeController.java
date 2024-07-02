package com.sparta.ottoon.like.controller;

import com.sparta.ottoon.comment.dto.CommentResponseDto;
import com.sparta.ottoon.common.CommonResponseDto;
import com.sparta.ottoon.like.service.LikeService;
import com.sparta.ottoon.post.dto.PostResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "createLikePost", description = "게시글 좋아요 생성 기능")
    @PostMapping("/post/{postId}/like")
    public ResponseEntity<String> likeOrUnlikePost(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(likeService.postlikeOrUnlike(userDetails.getUsername(), postId));
    }

    @Operation(summary = "createLikeComment", description = "댓글 좋아요 생성 기능")
    @PostMapping("/comment/{commentId}/like")
    public ResponseEntity<String> likeOrUnlikeComment(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(likeService.commentlikeOrUnlike(userDetails.getUsername() , commentId));
    }

    @Operation(summary = "getLikePost", description = "게시글 좋아요 조회 기능")
    @GetMapping("/post/{postId}/like")
    public ResponseEntity<CommonResponseDto<List<PostResponseDto>>> getLikePost(@AuthenticationPrincipal UserDetails userDetails,
                                                                               @PathVariable Long postId,
                                                                                @RequestParam(value = "page", defaultValue = "1") int page,
                                                                                @RequestParam(value = "size", defaultValue = "5") int size){
        CommonResponseDto<List<PostResponseDto>> commonResponseDto = new CommonResponseDto<>(
                "내가 좋아하는 게시글 조회 성공",
                HttpStatus.OK.value(),
                likeService.getLikePost(userDetails.getUsername(), postId, page-1, size)
        );
        return ResponseEntity.ok().body(commonResponseDto);
    }

    @Operation(summary = "getLikeComment", description = "댓글 좋아요 조회 기능")
    @GetMapping("/comment/{commentId}/like")
    public ResponseEntity<CommonResponseDto<List<CommentResponseDto>>> getLikeComment(@AuthenticationPrincipal UserDetails userDetails,
                                                                                      @PathVariable Long commentId,
                                                                                      @RequestParam(value = "page", defaultValue = "1") int page,
                                                                                      @RequestParam(value = "size", defaultValue = "5") int size){
        CommonResponseDto<List<CommentResponseDto>> commonResponseDto = new CommonResponseDto<>(
                "내가 좋아하는 댓글 조회 성공",
                HttpStatus.OK.value(),
                likeService.getLikeComment(userDetails.getUsername(), commentId, page-1, size)
        );
        return ResponseEntity.ok().body(commonResponseDto);
    }
}

package com.sparta.ottoon.like.controller;

import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.service.UserService;
import com.sparta.ottoon.comment.entity.Comment;
import com.sparta.ottoon.comment.repository.CommentRepository;
import com.sparta.ottoon.like.entity.LikeTypeEnum;
import com.sparta.ottoon.like.service.LikeService;
import com.sparta.ottoon.post.entity.Post;
import com.sparta.ottoon.post.repository.PostRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserService userService;

    public LikeController(LikeService likeService, PostRepository postRepository, CommentRepository commentRepository, UserService userService) {
        this.likeService = likeService;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    @Operation(summary = "createLikePost", description = "게시글 좋아요 생성 기능")
    @PostMapping("/post/{postId}")
    public ResponseEntity<String> likeOrUnlikePost(@AuthenticationPrincipal User user, @PathVariable Long postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Post post = postOptional.get();
        likeService.likeOrUnlike(user, post, null, LikeTypeEnum.POST_TYPE);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "createLikeComment", description = "댓글 좋아요 생성 기능")
    @PostMapping("/comment/{commentId}")
    public ResponseEntity<String> likeOrUnlikeComment(@AuthenticationPrincipal User user, @PathVariable Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Comment comment = commentOptional.get();
        likeService.likeOrUnlike(user, null, comment, LikeTypeEnum.COMMENT_TYPE);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

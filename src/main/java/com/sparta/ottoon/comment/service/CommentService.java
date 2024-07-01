package com.sparta.ottoon.comment.service;

import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.repository.UserRepository;
import com.sparta.ottoon.comment.dto.CommentRequestDto;
import com.sparta.ottoon.comment.dto.CommentResponseDto;
import com.sparta.ottoon.comment.entity.Comment;
import com.sparta.ottoon.comment.repository.CommentRepository;
import com.sparta.ottoon.common.exception.CustomException;
import com.sparta.ottoon.common.exception.ErrorCode;
import com.sparta.ottoon.post.entity.Post;
import com.sparta.ottoon.post.repository.PostRepository;
import com.sparta.ottoon.profile.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ProfileService profileService;

    public CommentResponseDto createComment(Long postId, CommentRequestDto commentRequestDto, String username) {
        // user 찾기
        User user = userRepository.findByUsername(username).orElseThrow(()->
                    new CustomException(ErrorCode.USER_NOT_FOUND));
        // post 찾기
        Post post = postRepository.findById(postId).orElseThrow(()->
                    new CustomException(ErrorCode.POST_NOT_FOUND));
        Comment comment = Comment.builder()
                .comment(commentRequestDto.getComment())
                .user(user)
                .post(post)
                .build();
        Comment saveComment= commentRepository.save(comment);
        return new CommentResponseDto(saveComment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComment(Long postId) {
        // post 찾기
        Post post = postRepository.findById(postId).orElseThrow(()->
                new CustomException(ErrorCode.POST_NOT_FOUND));
        // 해당 post 조회
        List<Comment> commentList = commentRepository.findByPostId(post.getId());
        return commentList.stream().map(CommentResponseDto::new).toList();
    }

    @Transactional(readOnly = true)
    public  CommentResponseDto getCommentById(Long postId, Long commentId){
        // post 찾기
        Post post = postRepository.findById(postId).orElseThrow(()->
                new CustomException(ErrorCode.POST_NOT_FOUND));

        return commentRepository.findWithLikeCountById(commentId).orElseThrow(()
                -> new CustomException(ErrorCode.FAIL_GETCOMMENT));
    }

    @Transactional
    public CommentResponseDto updateComment(Long postId, Long commentId,CommentRequestDto commentRequestDto, String username) {
        // username 찾기
        User user = userRepository.findByUsername(username).orElseThrow(()->
                new CustomException(ErrorCode.USER_NOT_FOUND));
        // 수정할 comment 찾기
        Comment comment = commentRepository.findByIdAndPostIdAndUserId(commentId,postId,user.getId()).orElseThrow(()->
                new CustomException(ErrorCode.FAIL_COMMENT));
        // 찾은 comment update
        comment.updateComment(commentRequestDto.getComment());
        return new CommentResponseDto(comment);
    }
    @Transactional
    public void deleteComment(Long commentId, String username){
        // 해당 comment 찾기
        Comment comment =commentRepository.findById(commentId).orElseThrow(()->
                new CustomException(ErrorCode.FAIL_GETCOMMENT));
        // 작성자 & admin 확인
        profileService.validateUserPermissions(comment.getUser(),username);
        // comment 삭제
        commentRepository.delete(comment);
    }
}

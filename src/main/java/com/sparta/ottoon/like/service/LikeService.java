package com.sparta.ottoon.like.service;

import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.repository.UserRepository;
import com.sparta.ottoon.comment.dto.CommentResponseDto;
import com.sparta.ottoon.comment.entity.Comment;
import com.sparta.ottoon.comment.repository.CommentRepository;
import com.sparta.ottoon.common.exception.CustomException;
import com.sparta.ottoon.common.exception.ErrorCode;
import com.sparta.ottoon.like.entity.Like;
import com.sparta.ottoon.like.entity.LikeTypeEnum;
import com.sparta.ottoon.like.repository.LikeRepository;
import com.sparta.ottoon.post.dto.PostResponseDto;
import com.sparta.ottoon.post.entity.Post;
import com.sparta.ottoon.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public String postlikeOrUnlike(String username, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new CustomException(ErrorCode.POST_NOT_FOUND));
        User user = userRepository.findByUsername(username).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        if(Objects.equals(post.getUser().getId(), user.getId())){
            throw new CustomException(ErrorCode.FAIL_LIKESELF);
        }
        Optional<Like> existingLike = likeRepository.findByPostAndUser(post, user);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return "게시글 좋아요 삭제 완료";
        } else {
            Like like = new Like(user, post, null, LikeTypeEnum.POST_TYPE);
            likeRepository.save(like);
            return "게시글 좋아요 완료";
        }
    }
    @Transactional
    public String commentlikeOrUnlike(String username,Long commentId){
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new CustomException(ErrorCode.FAIL_GETCOMMENT));
        User user = userRepository.findByUsername(username).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        if(Objects.equals(comment.getUser().getId(), user.getId())){
            throw new CustomException(ErrorCode.FAIL_COMMENTSELF);
        }
        Optional<Like> existingLike = likeRepository.findByCommentAndUser(comment, user);
        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return "댓글 좋아요 삭제 완료";
        } else {
            Like like = new Like(user, null, comment, LikeTypeEnum.COMMENT_TYPE);
            likeRepository.save(like);
            return "댓글 좋아요 완료";
        }
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getLikePost(String username, Long postId, int page, int size){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);

        Page<PostResponseDto> responseDtoPage = likeRepository.findLikedPostsByUserId(user.getId(), pageable);

        return responseDtoPage.getContent().stream()
                .map(m -> PostResponseDto.builder()
                        .postId(m.getPostId())
                        .contents(m.getContents())
                        .nickname(m.getNickname())
                        .likeCount(m.getLikeCount())
                        .createdAt(m.getCreatedAt())
                        .modifiedAt(m.getModifiedAt())
                        .build())
                .toList();
    }

    public List<CommentResponseDto> getLikeComment(String username, Long commentId, int page, int size){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.FAIL_GETCOMMENT));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size);

        Page<CommentResponseDto> responseDtoPage = likeRepository.findLikedCommentsByUserId(user.getId(), pageable);

        return responseDtoPage.getContent().stream()
                .map(m -> CommentResponseDto.builder()
                        .commentId(m.getCommentId())
                        .comment(m.getComment())
                        .nickname(m.getNickname())
                        .likeCount(m.getLikeCount())
                        .createdAt(m.getCreatedAt())
                        .modifiedAt(m.getModifiedAt())
                        .build())
                .toList();
    }

}

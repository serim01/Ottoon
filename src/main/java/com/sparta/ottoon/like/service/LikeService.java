package com.sparta.ottoon.like.service;

import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.repository.UserRepository;
import com.sparta.ottoon.comment.entity.Comment;
import com.sparta.ottoon.comment.repository.CommentRepository;
import com.sparta.ottoon.common.exception.CustomException;
import com.sparta.ottoon.common.exception.ErrorCode;
import com.sparta.ottoon.like.entity.Like;
import com.sparta.ottoon.like.entity.LikeTypeEnum;
import com.sparta.ottoon.like.repository.LikeRepository;
import com.sparta.ottoon.post.entity.Post;
import com.sparta.ottoon.post.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void likeOrUnlike(User user, Post post, Comment comment, LikeTypeEnum likeType) {
        if (likeType == LikeTypeEnum.POST_TYPE) {
            Optional<Like> existingLike = likeRepository.findByPostAndUser(post, user);
            if (existingLike.isPresent()) {
                likeRepository.delete(existingLike.get());
            } else {
                Like like = new Like(user, post, null, likeType);
                likeRepository.save(like);
            }
        } else if (likeType == LikeTypeEnum.COMMENT_TYPE) {
            Optional<Like> existingLike = likeRepository.findByCommentAndUser(comment, user);
            if (existingLike.isPresent()) {
                likeRepository.delete(existingLike.get());
            } else {
                Like like = new Like(user, null, comment, likeType);
                likeRepository.save(like);
            }
        } else {
            throw new CustomException(ErrorCode.INVALID_LIKE_TYPE);
        }
    }
}

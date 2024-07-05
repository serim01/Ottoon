package com.sparta.ottoon.post.service;

import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.entity.UserStatus;
import com.sparta.ottoon.auth.repository.UserRepository;
import com.sparta.ottoon.common.exception.CustomException;
import com.sparta.ottoon.common.exception.ErrorCode;
import com.sparta.ottoon.post.dto.PostRequestDto;
import com.sparta.ottoon.post.dto.PostResponseDto;
import com.sparta.ottoon.post.entity.Post;
import com.sparta.ottoon.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponseDto save(PostRequestDto postRequestDto, String username) {
        User user = findUserByUsername(username);
        Post post = Post.builder()
                .contents(postRequestDto.getContents())
                .user(user)
                .build();

        post = postRepository.save(post);

        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public PostResponseDto findById(long postId) {
        return postRepository.findWithLikeCountById(postId).orElseThrow(()
                -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> getAll(Pageable pageable) {
        Page<Post> postPage = postRepository.findAllByOrderByIsTopDescCreatedAtDesc(pageable);
       if (postPage.isEmpty()) {
            throw new CustomException(ErrorCode.POST_EMPTY);
        }
        return postPage.stream()
                .map(PostResponseDto::new)
                .toList();
    }

    @Transactional
    public PostResponseDto update(long postId, PostRequestDto postRequestDto, String username) {
        Post post = findPostById(postId);
        User user = findUserByUsername(username);

        // 본인 계정 혹은 관리자 계정이면 게시글 수정 가능
        if (user.getId().equals(post.getUser().getId()) || user.getStatus().equals(UserStatus.ADMIN)) {
            post.update(postRequestDto.getContents());

            return new PostResponseDto(post);
        } else {
            throw new CustomException(ErrorCode.BAD_AUTH_PUT);
        }
    }

    @Transactional
    public void delete(long postId, String username) {
        Post post = findPostById(postId);
        User user = findUserByUsername(username);

        // 본인 계정 혹은 관리자 계정이면 게시글 삭제 가능
        if (user.getId().equals(post.getUser().getId()) || user.getStatus().equals(UserStatus.ADMIN)) {
            postRepository.delete(post);
        } else {
            throw new CustomException(ErrorCode.BAD_AUTH_DELETE);
        }
    }

    private Post findPostById(long postId) {
        return postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.BAD_POST_ID));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}

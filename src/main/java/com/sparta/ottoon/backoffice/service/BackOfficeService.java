package com.sparta.ottoon.backoffice.service;

import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.entity.UserStatus;
import com.sparta.ottoon.auth.repository.UserRepository;
import com.sparta.ottoon.backoffice.dto.EditRequestDto;
import com.sparta.ottoon.common.exception.CustomException;
import com.sparta.ottoon.common.exception.ErrorCode;
import com.sparta.ottoon.post.entity.Post;
import com.sparta.ottoon.post.entity.PostStatus;
import com.sparta.ottoon.post.repository.PostRepository;
import com.sparta.ottoon.profile.dto.ProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BackOfficeService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;


    public List<ProfileResponseDto> getAllUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream().map(ProfileResponseDto::new).toList();
    }


    @Transactional
    public ResponseEntity<?> editUserRole(String username, EditRequestDto editRequestDto) {
        User user = findUserByUsername(username);


        if (!user.getStatus().equals(UserStatus.WITHDRAW)) {
            switch (editRequestDto.getUserStatus()) {
                case "ACTIVE" -> user.setStatus(UserStatus.ACTIVE);
                case "ADMIN" -> user.setStatus(UserStatus.ADMIN);
                case "BLOCK" -> user.setStatus(UserStatus.BLOCK);
                case "DELETE" ->{user.setStatus(UserStatus.DELETE);
                    userRepository.delete(user);}
                default -> throw new CustomException(ErrorCode.NOT_ENUM_VALUE);
            }
        } else {
            switch (editRequestDto.getUserStatus()) {
                case "DELETE" -> userRepository.delete(user);
                default -> throw new CustomException(ErrorCode.CANNOT_EDIT);
            }
        }

        if (!user.getStatus().equals(UserStatus.DELETE)) {
            return ResponseEntity.ok(user.getUsername() + "의 권한이" + user.getStatus() + " 로 변경되었습니다.");
        }
        return ResponseEntity.ok(user.getUsername() + " 회원이 삭제되었습니다.");
    }


    @Transactional
    public ResponseEntity<String> noticePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(()->new CustomException(ErrorCode.FAIL_FIND_POST));
        if (post.getPostStatus().equals(PostStatus.NOTICE)) {
            post.setPostStatus(PostStatus.GENERAL);
            post.setTop(true);
            return ResponseEntity.ok("해당 게시물이 일반 게시물로 변경되었습니다.");
        }
        post.setPostStatus(PostStatus.NOTICE);
        post.setTop(false);
        return ResponseEntity.ok("해당 게시물이 공지 게시물로 변경되었습니다.");

    }


    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }


}

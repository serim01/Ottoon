package com.sparta.ottoon.backoffice;

import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.entity.UserStatus;
import com.sparta.ottoon.auth.repository.UserRepository;
import com.sparta.ottoon.common.exception.CustomException;
import com.sparta.ottoon.common.exception.ErrorCode;
import com.sparta.ottoon.post.entity.Post;
import com.sparta.ottoon.post.entity.PostStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BackOfficeService {

    private final UserRepository userRepository;
//    private final PostRepository postRepository;


//    public List<UserResponseDto> getAllUsers() {
//        List<User> userList = userRepository.findAll();
//
//        return userList.stream().map(userResponseDto::new).toList();
//
//    }

    @Transactional
    public UserResponseDto editUserRole(String username, EditRequestDto editRequestDto) {
        User user = findUserByUsername(username);

        if (!user.getStatus().equals(UserStatus.WITHDRAW)) {
            switch (editRequestDto.getUserStatus()) {
                case "ACTIVE" -> user.setStatus(UserStatus.ACTIVE);
                case "ADMIN" -> user.setStatus(UserStatus.ADMIN);
                case "BLOCK" -> user.setStatus(UserStatus.BLOCK);
                case "DELETE" -> userRepository.delete(user);
                default -> throw new CustomException(ErrorCode.NOTENUMVALUE);
            }
        }
        UserResponseDto userResponseDto = new UserResponseDto(user);
        return userResponseDto;
    }


//    @Transactional
//    public ResponseEntity<String> noticePost(Long postId) {
//        Post post = postRepository.findById(postId).orElseThrow(ErrorCode.FAILFINDPOST);
//        if (post.getPostStatus().equals(PostStatus.NOTICE)) {
//            post.setPostStatus(PostStatus.GENERAL);
//            post.set_top(true);
//            return ResponseEntity.ok("해당 게시물이 일반 게시물로 변경되었습니다.");
//        }
//        post.setPostStatus(PostStatus.NOTICE);
//        post.set_top(false);
//        return ResponseEntity.ok("해당 게시물이 공지 게시물로 변경되었습니다.");
//
//    }


    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.FAILFINDUSER));
    }


}

package com.sparta.ottoon.follow.controller;

import com.sparta.ottoon.auth.service.UserDetailsImpl;
import com.sparta.ottoon.follow.dto.FollowResponseDto;
import com.sparta.ottoon.follow.service.FollowService;
import com.sparta.ottoon.profile.dto.ProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follows/users/{userId}")
    public ResponseEntity<FollowResponseDto<Void>> followUser(@PathVariable long userId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails){
        ProfileResponseDto profileResponseDto = followService.followUser(userId, userDetails.getUser());
        return ResponseEntity.ok().body(new FollowResponseDto<>(HttpStatus.OK.value(),
                profileResponseDto.getUsername() + "님을 팔로우 했습니다.",
                null));
    }

    @PutMapping("/follows/users/{userId}")
    public ResponseEntity<FollowResponseDto<Void>> followCancel(@PathVariable long userId,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        ProfileResponseDto profileResponseDto = followService.followCancle(userId, userDetails.getUser());
        return ResponseEntity.ok().body(new FollowResponseDto<>(HttpStatus.OK.value(),
                profileResponseDto.getUsername() + "님의 팔로우를 취소 했습니다.",
                null));
    }
}

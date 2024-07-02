package com.sparta.ottoon.profile.controller;

import com.sparta.ottoon.common.CommonResponseDto;
import com.sparta.ottoon.profile.dto.ProfileRequestDto;
import com.sparta.ottoon.profile.dto.ProfileResponseDto;
import com.sparta.ottoon.profile.dto.UserPwRequestDto;
import com.sparta.ottoon.profile.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Profile API", description = "Profile API 입니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class ProfileController {

    private final ProfileService userService;

    @Operation(summary = "getUser", description = "프로필 조회 기능입니다.")
    @GetMapping("/{userName}")
    public ResponseEntity<CommonResponseDto<ProfileResponseDto>> getUser(@PathVariable String userName){
        CommonResponseDto<ProfileResponseDto> commonResponseDto = new CommonResponseDto<>(
                "프로필 조회 성공",
                HttpStatus.OK.value(),
                userService.getUser(userName)
        );
        return ResponseEntity.ok().body(commonResponseDto);
    }

    @Operation(summary = "updateUser", description = "프로필 수정 기능입니다.")
    @PostMapping("/{userName}")
    public ResponseEntity<CommonResponseDto<ProfileResponseDto>> updateUser(@PathVariable String userName,
                                                         @AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestBody ProfileRequestDto requestDto){
        CommonResponseDto<ProfileResponseDto> commonResponseDto = new CommonResponseDto<>(
                "프로필 수정 성공",
                HttpStatus.OK.value(),
                userService.updateUser(userName, userDetails.getUsername(),requestDto)
        );
        return ResponseEntity.ok().body(commonResponseDto);
    }

    @Operation(summary = "updateUserPassword", description = "비밀번호 변경 기능입니다.")
    @PostMapping("/{userName}/password")
    public ResponseEntity<CommonResponseDto<Void>> updateUserPassword(@PathVariable String userName,
                                                     @AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestBody @Valid UserPwRequestDto requestDto){
        userService.updateUserPassword(userName, userDetails.getUsername(), requestDto);
        CommonResponseDto<Void> commonResponseDto = new CommonResponseDto<>(
                "프로필 비밀번호 변경 성공",
                HttpStatus.OK.value(),
                null
        );
        return ResponseEntity.ok().body(commonResponseDto);
    }

}

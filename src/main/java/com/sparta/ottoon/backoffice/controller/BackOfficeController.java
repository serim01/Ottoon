package com.sparta.ottoon.backoffice.controller;

import com.sparta.ottoon.auth.entity.UserStatus;
import com.sparta.ottoon.backoffice.dto.EditRequestDto;
import com.sparta.ottoon.backoffice.service.BackOfficeService;
import com.sparta.ottoon.profile.dto.ProfileResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
@Secured(UserStatus.Authority.ADMIN)
public class BackOfficeController {

    private final BackOfficeService backOfficeService;

    /*
    전체 회원 조회
     */
    @Operation(summary = "전체 회원 조회", description = "사용자 전제 조회기능입니다.")
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        List<ProfileResponseDto> response = backOfficeService.getAllUsers();
        if (response == null) {
            return ResponseEntity.ok("가입된 사용자가 없습니다.");
        }
        return ResponseEntity.ok(response);
    }

    /*
    특정회원 권한 수정
     */
    @Operation(summary = "권한 수정", description = "특정 회원 권한 수정 기능입니다.")
    @PutMapping("/users/{username}/edit")
    public ResponseEntity<?> editUserRole(@PathVariable String username,
                                          @RequestBody @Valid EditRequestDto editRequestDto) {
        return backOfficeService.editUserRole(username, editRequestDto);
    }

    /*
    특정 게시글 상단 고정
     */
    @Operation(summary = "공지 등록", description = "공지글 등록 기능입니다.")
    @PostMapping("/posts/{postId}/notice")
    public ResponseEntity<String> noticePost(@PathVariable Long postId) {
        return backOfficeService.noticePost(postId);
    }


}

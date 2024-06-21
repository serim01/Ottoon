package com.sparta.ottoon.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.ottoon.auth.dto.LoginRequestDto;
import com.sparta.ottoon.auth.dto.SignupRequestDto;
import com.sparta.ottoon.auth.jwt.JwtUtil;
import com.sparta.ottoon.auth.service.SocialService;
import com.sparta.ottoon.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "USER API", description = "USER API 입니다")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    final private UserService userService;
    final private SocialService socialService;

    /**
     * 회원가입 API
     * @param requestDto : 회원가입 정보가 담긴 dto
     * @param bindingResult : valid를 검사하여 오류가 있으면 알려준다.
     * @return
     */
    @Operation(summary = "signup", description = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        // @Valid 에러가 있는지 확인
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fieldError.getDefaultMessage());
            }
        }

        userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입에 성공하였습니다.");
    }

    /**
     * Swagger 테스트를 위한 로그인 api (실제 로그인이 이루어지는 api가 아니다)
     * @param loginRequestDto
     * @return
     */
    @Operation(summary = "login test version", description = "더미 로그인")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok().body("swagger 상 api 사용을 위한 로그인");
    }

    /**
     * 로그아웃
     * @param userDetails
     * @return
     */
    @Operation(summary = "logout", description = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetails userDetails) {
        userService.logout(userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body("로그아웃 하였습니다.");
    }

    /**
     * kakao 소셜 로그인
     * @param code
     * @param response
     * @return
     * @throws JsonProcessingException
     */
    @Operation(summary = "kakao login", description = "카카오 소셜 로그인")
    @GetMapping("/kakao")
    public ResponseEntity<String> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        String token = socialService.kakaoLogin(code);

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token); // response header에 access token 넣기
        return ResponseEntity.status(HttpStatus.OK).body("카카오 로그인 하였습니다.");
    }
}

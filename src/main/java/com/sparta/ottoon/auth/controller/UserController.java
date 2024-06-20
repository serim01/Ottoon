package com.sparta.ottoon.auth.controller;

import com.sparta.ottoon.auth.dto.LoginRequestDto;
import com.sparta.ottoon.auth.dto.SignupRequestDto;
import com.sparta.ottoon.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    final private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fieldError.getDefaultMessage());
            }
        }

        userService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입에 성공하였습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto){
        return ResponseEntity.ok().body("swagger 상 api 사용을 위한 로그인");
    }
}

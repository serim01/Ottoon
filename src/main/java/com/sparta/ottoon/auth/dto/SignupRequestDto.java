package com.sparta.ottoon.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원가입 요청을 보낼 때의 DTO
 */
@Getter
@Setter
public class SignupRequestDto {
    @NotBlank
    @Pattern(regexp = "(?=.*[a-z])(?=.*[0-9]).{4,10}",
            message = "아이디는 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 이루어져 있어야 합니다.")
    private String username;

    @NotBlank
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank
    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}",
            message = "비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 이루어져 있어야 합니다.")
    private String password;

    private String adminToken;

    private boolean admin;
}

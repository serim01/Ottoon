package com.sparta.ottoon.profile.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserPwRequestDto {
    private String oldPassword;

    @Pattern(regexp = "(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}",
            message = "비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 이루어져 있어야 합니다.")
    private String newPassword;
}

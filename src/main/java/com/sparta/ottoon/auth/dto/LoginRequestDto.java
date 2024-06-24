package com.sparta.ottoon.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Login 요청을 보낼때의 DTO
 */
@Getter
@AllArgsConstructor
public class LoginRequestDto {
    private String username;
    private String password;
}

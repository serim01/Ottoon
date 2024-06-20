package com.sparta.ottoon.auth.dto;

import lombok.Getter;

/**
 * Login 요청을 보낼때의 DTO
 */
@Getter
public class LoginRequestDto {
    private String username;
    private String password;
}

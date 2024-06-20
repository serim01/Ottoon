package com.sparta.ottoon.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATE_UESR(400, "중복된 사용자가 존재합니다."),
    DUPLICATE_EMAIL(400, "중복된 이메일이 존재합니다."),
    INCORRECT_ADMIN(400, "관리자 암호가 일치하지 않습니다."),
    FAIL(500, "실패했습니다."),
    USER_NOT_FOUND(400, "해당 아이디의 유저를 찾지 못했습니다."),
    INCORRECT_PASSWORD(400, "입력하신 비밀번호가 일치하지 않습니다."),
    DUPLICATE_PASSWORD(400, "기존 비밀번호와 동일한 비밀번호입니다."),
    LAST3_PASSWORD(400, "최근 사용한 세 개의 비밀번호와 다르게 설정해야 합니다.");

    private int status;
    private String msg;
}
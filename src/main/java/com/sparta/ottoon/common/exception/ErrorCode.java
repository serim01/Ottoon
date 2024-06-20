package com.sparta.ottoon.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // post
    BAD_POST_ID(400, "게시글 ID를 찾을 수 없습니다."),
    BAD_AUTH_PUT(400, "작성자만 수정할 수 있습니다."),
    BAD_AUTH_DELETE(400, "작성자만 삭제할 수 있습니다."),
    //USER_NOT_FOUND(400, "등록되지 않은 사용자입니다."),
    DUPLICATE_UESR(400, "중복된 사용자가 존재합니다."),
    DUPLICATE_EMAIL(400, "중복된 이메일이 존재합니다."),
    INCORRECT_ADMIN(400, "관리자 암호가 일치하지 않습니다."),
    FAIL(500, "실패했습니다."),
    USER_NOT_FOUND(400, "해당 아이디의 유저를 찾지 못했습니다."),
    INCORRECT_PASSWORD(400, "입력하신 비밀번호가 일치하지 않습니다."),
    DUPLICATE_PASSWORD(400, "기존 비밀번호와 동일한 비밀번호입니다."),
    LAST3_PASSWORD(400, "최근 사용한 세 개의 비밀번호와 다르게 설정해야 합니다."),
    USER_FORBIDDEN(403, "본인 프로필만 수정이 가능합니다.");

    private int status;
    private String msg;
}
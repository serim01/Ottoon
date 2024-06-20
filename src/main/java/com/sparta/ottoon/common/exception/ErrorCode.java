package com.sparta.ottoon.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    FAIL(500, "실패했습니다."),

    // post
    BAD_POST_ID(400, "게시글 ID를 찾을 수 없습니다."),
    BAD_AUTH_PUT(400, "작성자만 수정할 수 있습니다."),
    BAD_AUTH_DELETE(400, "작성자만 삭제할 수 있습니다."),
    USER_NOT_FOUND(400, "등록되지 않은 사용자입니다.");

    private int status;
    private String msg;
}
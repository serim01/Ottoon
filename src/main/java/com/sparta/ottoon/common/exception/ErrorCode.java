package com.sparta.ottoon.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    FAIL(500, "실패했습니다."),
    FAILFINDUSER(400, "해당 유저를 찾을 수 없습니다."),
    FAILFINDPOST(400, "해당 게시물을 찾을 수 없습니다."),
    BADREQUEST(400, "잘못된 요청입니다." ),
    NOTENUMVALUE(400, "잘못된 입력값입니다." );
    private int status;
    private String msg;
}
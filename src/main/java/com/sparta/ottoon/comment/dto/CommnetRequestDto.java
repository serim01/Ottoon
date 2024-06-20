package com.sparta.ottoon.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommnetRequestDto {
    private Long postId; //댓글이 생성될 게시글
    private String comment; //댓글 내용
}

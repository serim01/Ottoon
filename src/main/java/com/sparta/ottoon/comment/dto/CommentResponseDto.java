package com.sparta.ottoon.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.sparta.ottoon.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {
    private Long commentId;
    private String comment;
    private String nickname;
    private Integer likeCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;

    public CommentResponseDto(Comment comment) {
        this.nickname = comment.getUser().getNickname();
        this.commentId = comment.getId();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }

    @QueryProjection
    public CommentResponseDto(Long commentId, String comment, String nickname, Integer likeCount,
                              LocalDateTime createdAt, LocalDateTime modifiedAt){
        this.commentId = commentId;
        this.comment = comment;
        this.nickname = nickname;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}

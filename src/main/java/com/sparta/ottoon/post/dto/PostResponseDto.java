package com.sparta.ottoon.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import com.sparta.ottoon.post.entity.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private String message;
    private int statusCode;
    private Long postId;
    private String nickname;
    @NotBlank(message = "내용을 입력해 주세요")
    private String contents;
    private Integer likeCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modifiedAt;

    //게시글 등록, 수정, 조회
    public static PostResponseDto toDto(String message, int statusCode, Post post) {
        return PostResponseDto.builder()
                .message(message)
                .statusCode(statusCode)
                .postId(post.getId())
                .contents(post.getContents())
                .nickname(post.getUser().getNickname())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();
    }

    // 게시글 삭제
    public static PostResponseDto toDeleteResponse(String message, int statusCode) {
        return PostResponseDto.builder()
                .message(message)
                .statusCode(statusCode)
                .build();
    }

    //부분 조회시 사용
    @QueryProjection
    public PostResponseDto(Long postId, String contents, Integer likeCount, String nickname,
                           LocalDateTime createdAt, LocalDateTime modifiedAt){
        this.message = "부분 게시글 조회 완료";
        this.statusCode = 200;
        this.postId = postId;
        this.nickname = nickname;
        this.contents = contents;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}

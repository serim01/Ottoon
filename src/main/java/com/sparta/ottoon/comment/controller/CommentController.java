package com.sparta.ottoon.comment.controller;

import com.sparta.ottoon.comment.dto.CommentRequestDto;
import com.sparta.ottoon.comment.dto.CommentResponseDto;
import com.sparta.ottoon.comment.service.CommentService;
import com.sparta.ottoon.common.CommonResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comment API", description = "Comment API 입니다")
@RestController
@RequestMapping("/api/post/{postId}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //comment 생성
    @Operation(summary = "createComment", description = "댓글 생성 기능입니다.")
    @PostMapping("/comment")
    public ResponseEntity<CommonResponseDto<CommentResponseDto>> createComment(@PathVariable Long postId,
                                                                              @RequestBody CommentRequestDto commentRequestDto,
                                                                              @AuthenticationPrincipal UserDetails userDetails){
        CommonResponseDto<CommentResponseDto> commonResponseDto = new CommonResponseDto<>(
                "댓글 등록 성공",
                HttpStatus.CREATED.value(),
                commentService.createComment(postId, commentRequestDto, userDetails.getUsername())
        );
        return  ResponseEntity.status(HttpStatus.CREATED).body(commonResponseDto);
    }

    //comment 조회
    @Operation(summary = "getComment", description = "댓글 조회 기능입니다.")
    @GetMapping("/comment")
    public ResponseEntity<CommonResponseDto<List<CommentResponseDto>>> getComment(@PathVariable Long postId){
        CommonResponseDto<List<CommentResponseDto>> commonResponseDto = new CommonResponseDto<>(
                "댓글 조회 성공",
                HttpStatus.OK.value(),
                commentService.getComment(postId)
        );
            return ResponseEntity.ok().body(commonResponseDto);
        }

    @Operation(summary = "getCommentById", description = "댓글 선택 조회 기능입니다.")
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<CommonResponseDto<CommentResponseDto>> getCommentById(@PathVariable Long postId, @PathVariable Long commentId){
        CommonResponseDto<CommentResponseDto> commonResponseDto = new CommonResponseDto<>(
                "댓글 선택 조회 성공",
                HttpStatus.OK.value(),
                commentService.getCommentById(postId, commentId)
        );
        return ResponseEntity.ok().body(commonResponseDto);
    }

    //comment 수정
    @Operation(summary = "updateComment", description = "댓글 수정 기능입니다.")
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<CommonResponseDto<CommentResponseDto>> updateComment(@PathVariable (value = "postId")Long postId, @PathVariable (value = "commentId")Long commentId,
                                                            @RequestBody CommentRequestDto commentRequestDto,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        CommonResponseDto<CommentResponseDto> commonResponseDto = new CommonResponseDto<>(
                "댓글 수정 성공",
                HttpStatus.OK.value(),
                commentService.updateComment(postId, commentId,commentRequestDto, userDetails.getUsername())
        );
        return ResponseEntity.ok().body(commonResponseDto);
    }

    //comment 삭제
    @Operation(summary = "deleteComment", description = "댓글 삭제 기능입니다.")
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteComment(@PathVariable (value = "postId")Long postId,@PathVariable (value = "commentId") Long commentId,
                                              @AuthenticationPrincipal UserDetails userDetails){
        commentService.deleteComment(commentId,userDetails.getUsername());
        CommonResponseDto<Void> commonResponseDto = new CommonResponseDto<>(
                "댓글 삭제 성공",
                HttpStatus.NO_CONTENT.value(),
                null
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(commonResponseDto);
    }
}

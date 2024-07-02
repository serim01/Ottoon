package com.sparta.ottoon.post.controller;

import com.sparta.ottoon.common.CommonResponseDto;
import com.sparta.ottoon.post.dto.PostRequestDto;
import com.sparta.ottoon.post.dto.PostResponseDto;
import com.sparta.ottoon.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Post API", description = "Post API 입니다")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // 게시글 등록
    @Operation(summary = "createPost", description = "게시글 생성 기능입니다.")
    @PostMapping("/posts")
    public ResponseEntity<CommonResponseDto<PostResponseDto>> create(@Valid @RequestBody PostRequestDto postRequestDto,
                                                                     @AuthenticationPrincipal UserDetails userDetails) {
        CommonResponseDto<PostResponseDto> commonResponseDto = new CommonResponseDto<>(
                "게시글 등록 성공",
                HttpStatus.CREATED.value(),
                postService.save(postRequestDto, userDetails.getUsername())
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponseDto);
    }

    // 게시글 전체 조회
    @Operation(summary = "getPostAll", description = "게시글 전체 조회 기능입니다.")
    @GetMapping("/posts")
//    public ResponseEntity<Map<String, Object>> getAll() {
    public ResponseEntity<CommonResponseDto<List<PostResponseDto>>> getAll(@RequestParam(value = "page", defaultValue = "1") int page,
                                                                           @RequestParam(value = "size", defaultValue = "5") int size) {
        CommonResponseDto<List<PostResponseDto>> commonResponseDto = new CommonResponseDto<>(
                "게시글 전체 조회 성공",
                HttpStatus.OK.value(),
                postService.getAll(page - 1, size)
        );
        return ResponseEntity.ok().body(commonResponseDto);

    }

    // 게시글 부분 조회
    @Operation(summary = "getPostSelect", description = "게시글 부분 조회 기능입니다.")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<CommonResponseDto<PostResponseDto>> findById(@PathVariable(name = "postId") long postId) {
        CommonResponseDto<PostResponseDto> commonResponseDto = new CommonResponseDto<>(
                "게시글 부분 조회 성공",
                HttpStatus.OK.value(),
                postService.findById(postId)
        );
        return ResponseEntity.ok().body(commonResponseDto);
    }

    // 게시글 수정
    @Operation(summary = "updatePost", description = "게시글 수정 기능입니다.")
    @PutMapping("/posts/{postId}")
    public ResponseEntity<CommonResponseDto<PostResponseDto>> update(@PathVariable(name = "postId") long postId,
                                                  @Valid @RequestBody PostRequestDto postRequestDto,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        CommonResponseDto<PostResponseDto> commonResponseDto = new CommonResponseDto<>(
                "게시글 수정 성공",
                HttpStatus.OK.value(),
                postService.update(postId, postRequestDto, userDetails.getUsername())
        );
        return ResponseEntity.ok().body(commonResponseDto);
    }

    //게시글 삭제
    @Operation(summary = "deletePost", description = "게시글 삭제 기능입니다.")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<CommonResponseDto<Void>> delete(@PathVariable(name = "postId") long postId,
                                                      @AuthenticationPrincipal UserDetails userDetails) {

        postService.delete(postId, userDetails.getUsername());
        CommonResponseDto<Void> commonResponseDto = new CommonResponseDto<>(
                "게시글 삭제 성공",
                HttpStatus.NO_CONTENT.value(),
                null
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(commonResponseDto);
    }
}

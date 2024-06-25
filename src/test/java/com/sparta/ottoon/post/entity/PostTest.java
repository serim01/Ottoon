package com.sparta.ottoon.post.entity;

import com.sparta.ottoon.fixtureMonkey.FixtureMonkeyUtil;
import com.sparta.ottoon.post.dto.PostRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
@DisplayName("게시글 entity 테스트")
public class PostTest {
    Post post;

    @BeforeEach
    void setUp() {
        post = FixtureMonkeyUtil.Entity.toPost();
    }

    @DisplayName("게시글 등록")
    @Test
    void test1() {
        // given
        String content = "content";

        PostRequestDto requestDto = new PostRequestDto("게시글 등록");
        // when
        post = new Post(requestDto.getContents());

        // then
        assertEquals(requestDto.getContents(), post.getContents());
    }

    @DisplayName("게시글 업데이트")
    @Test
    void test2() {
        // given
        String content = "updateContent";

        PostRequestDto requestDto = new PostRequestDto("게시글 업데이트");
        // when
        post.update(requestDto.getContents());

        // then
        assertEquals(requestDto.getContents(), post.getContents());
    }
}

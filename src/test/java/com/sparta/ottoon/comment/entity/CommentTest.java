package com.sparta.ottoon.comment.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentTest {
    @Test
    @DisplayName("updateComment() 테스트")
    void test1() {
        // Given
        Comment comment = Comment.builder()
                .comment("this is test comment")
                .build();
        String updateString = "update!!!!";

        // When
        comment.updateComment(updateString);

        // Then
        assertEquals(updateString, comment.getComment());
    }
}
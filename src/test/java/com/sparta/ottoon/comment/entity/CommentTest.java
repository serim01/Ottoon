package com.sparta.ottoon.comment.entity;

import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.post.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommentTest {
    @Test
    @DisplayName("updateComment() 테스트")
    void test1() {
        // Given
        User user = new User();
        Post post = new Post();
        Comment comment = new Comment.CommentBuilder()
                .comment("this is test comment")
                .user(user)
                .post(post)
                .build();

        // When
        String updateComment = "update!!!!!";
        comment.updateComment(updateComment);

        // Then
        assertEquals(comment.getComment(), updateComment);
    }
}
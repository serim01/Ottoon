package com.sparta.ottoon.comment.service;

import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.entity.UserStatus;
import com.sparta.ottoon.auth.repository.UserRepository;
import com.sparta.ottoon.comment.dto.CommentRequestDto;
import com.sparta.ottoon.comment.dto.CommentResponseDto;
import com.sparta.ottoon.comment.entity.Comment;
import com.sparta.ottoon.comment.repository.CommentRepository;
import com.sparta.ottoon.post.entity.Post;
import com.sparta.ottoon.post.repository.PostRepository;
import com.sparta.ottoon.profile.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@SpringBootTest
class CommentServiceIntegrationTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private ProfileService profileService;


    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        user = new User("testUser", "testNickname", "testPassword", "test@email.com", UserStatus.ACTIVE);
        post = new Post("this is test post");
    }

    @Test
    @DisplayName("createComment 테스트")
    void test1() {
        // Given
        Long postId = 1L;
        CommentRequestDto commentRequestDto = new CommentRequestDto();
        commentRequestDto.setComment("this is test comment");
        Comment comment = Comment.builder()
                        .user(user)
                        .post(post)
                        .comment(commentRequestDto.getComment())
                        .build();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);

        // When
        CommentResponseDto commentResponseDto = commentService.createComment(postId, commentRequestDto, user.getUsername());

        // Then
        assertEquals(commentRequestDto.getComment(), commentResponseDto.getComment());

        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).save(Mockito.any(Comment.class));
    }

    @Test
    @DisplayName("getComment() 테스트")
    void test2() {
        // Given
        Long postId = 1L;
        List<Comment> commentList = new ArrayList<>();
        Comment comment1 = Comment.builder().comment("comment1").user(user).build();
        Comment comment2 = Comment.builder().comment("comment2").user(user).build();
        commentList.add(comment1);
        commentList.add(comment2);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findByPostId(null)).thenReturn(commentList);

        // When
        List<CommentResponseDto> result = commentService.getComment(postId);

        // Then
        assertEquals(commentList.size(), result.size());
        assertEquals(commentList.get(0).getComment(), result.get(0).getComment());
        assertEquals(commentList.get(1).getComment(), result.get(1).getComment());

        verify(postRepository, times(1)).findById(postId);
        verify(commentRepository, times(1)).findByPostId(null);
    }

    @Test
    @DisplayName("updateComment() 테스트")
    void test3() {
        // Given
        Long postId = 1L;
        Long commentId = 1L;
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setComment("this is update comment");
        Comment comment = Comment.builder()
                        .user(user)
                        .post(post)
                        .comment("this is test comment")
                        .build();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(commentRepository.findByIdAndPostIdAndUserId(commentId, postId, null)).thenReturn(Optional.of(comment));

        // When
        CommentResponseDto responseDto = commentService.updateComment(postId, commentId, requestDto, user.getUsername());

        // Then
        assertEquals(requestDto.getComment(), responseDto.getComment());

        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(commentRepository, times(1)).findByIdAndPostIdAndUserId(commentId, postId, null);
    }

    @Test
    @DisplayName("deleteComment() 테스트")
    void test4() {
        // Given
        Long commentId = 1L;
        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .build();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // When
        commentService.deleteComment(commentId, user.getUsername());

        // Then
        verify(commentRepository, times(1)).findById(commentId);
        verify(profileService, times(1)).validateUserPermissions(user, user.getUsername());
        verify(commentRepository, times(1)).delete(comment);
    }
}
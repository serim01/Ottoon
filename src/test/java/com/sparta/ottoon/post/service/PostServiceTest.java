package com.sparta.ottoon.post.service;

import com.sparta.ottoon.OttoonApplicationTests;
import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.entity.UserStatus;
import com.sparta.ottoon.auth.repository.UserRepository;
import com.sparta.ottoon.common.exception.CustomException;
import com.sparta.ottoon.common.exception.ErrorCode;
import com.sparta.ottoon.fixtureMonkey.FixtureMonkeyUtil;
import com.sparta.ottoon.post.dto.PostRequestDto;
import com.sparta.ottoon.post.dto.PostResponseDto;
import com.sparta.ottoon.post.entity.Post;
import com.sparta.ottoon.post.repository.PostRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("게시글 서비스 테스트")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostServiceTest extends OttoonApplicationTests {
    private List<Post> posts;
    User user;

    @Autowired
    PostService PostService;

    @Autowired
    PostRepository PostRepository;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        posts = PostRepository.saveAll(getPostDataInit(5));
        user = posts.get(0).getUser();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    String content = FixtureMonkeyUtil.getRandomStringArbitrary(5, 20).sample();

    @DisplayName("게시물 생성 - 정상 동작")
    @Transactional
    @Test
    void test1() {
        // given
        PostRequestDto requestDto = FixtureMonkeyUtil.monkey()
                .giveMeBuilder(PostRequestDto.class)
                .set("contents", content)
                .sample();
        // when
        PostResponseDto responseDto = PostService.save(requestDto);

        // then
        Post createPost = PostRepository.findById(responseDto.getPostId()).orElse(null);

        assert createPost != null;
        assertEquals(requestDto.getContents(), createPost.getContents());
        assertEquals(user.getUsername(), createPost.getUser().getUsername());
    }

    @Nested
    @DisplayName("게시물 조회")
    @Order(1)
    class Read {
        // given
        @DisplayName("게시물 전체 조회")
        @Test
        void test1() {
            // when
            List<PostResponseDto> responseDto = PostService.getAll(1);
            List<PostResponseDto> sortedPosts = posts.stream()
                    .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                    .map(post -> PostResponseDto.toDto("전체 게시글 조회 완료", 200, post))
                    .toList();
            // then
            assertEquals(sortedPosts.get(0).getContents(), responseDto.get(0).getContents());
        }

        @DisplayName("게시물 선택 조회")
        @Test
        void test4() {
            // given
            Post post = posts.get(0);
            Long postId = post.getId();

            // when
            PostResponseDto responseDto = PostService.findById(postId);

            // then
            assertEquals(postId, responseDto.getPostId());
            assertEquals(post.getContents(), responseDto.getContents());
        }
    }

    @Nested
    @DisplayName("게시물 업데이트")
    @Order(2)
    class Update {
        @DisplayName("게시물 업데이트_본인")
        @Test
        @Order(1)
        void test1() {
            // given
            Long postId = posts.get(0).getId();

            PostRequestDto requestDto = FixtureMonkeyUtil.monkey()
                    .giveMeBuilder(PostRequestDto.class)
                    .set("contents", content)
                    .sample();

            // when
            PostResponseDto responseDto = PostService.update(postId, requestDto);

            // then
            assertEquals(content, requestDto.getContents());
        }

        @DisplayName("게시물 업데이트_Admin")
        @Test
        @Order(2)
        void test2() {
            // given
            Long postId = posts.get(0).getId();
            User user = userRepository.save(new User("testAdmin", "testNickname", "testPassword", "test@email.com", UserStatus.ADMIN));
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            PostRequestDto requestDto = FixtureMonkeyUtil.monkey()
                    .giveMeBuilder(PostRequestDto.class)
                    .set("contents", content)
                    .sample();

            // when
            PostResponseDto responseDto = PostService.update(postId, requestDto);

            // then
            assertEquals(content, requestDto.getContents());
        }

        @DisplayName("게시물 업데이트_다른 사람")
        @Test
        @Order(3)
        void test3() {
            // given
            Long postId = posts.get(0).getId();
            User another = userRepository.save(new User("Another_test", "testNickname", "testPassword", "test@email.com", UserStatus.ACTIVE));
            Authentication authentication = new UsernamePasswordAuthenticationToken(another.getUsername(), another.getPassword());
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            PostRequestDto requestDto = FixtureMonkeyUtil.monkey()
                    .giveMeBuilder(PostRequestDto.class)
                    .set("contents", content)
                    .sample();

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> PostService.update(postId, requestDto));

            // then
            assertEquals(exception.getErrorCode(), ErrorCode.BAD_AUTH_PUT);
        }
    }
    @Nested
    @DisplayName("게시물 삭제")
    @Order(3)
    class Delete {
        @DisplayName("게시물 삭제_본인")
        @Test
        public void test1() {
            // given
            Long postId = posts.get(0).getId();
            // when
            PostService.delete(postId);

            // then
            CustomException exception = assertThrows(CustomException.class,
                    () -> PostService.findById(postId));

            assertEquals(exception.getErrorCode(), ErrorCode.BAD_POST_ID);
        }
        @DisplayName("게시물 삭제_다른사람")
        @Test
        public void test2() {
            // given
            Long postId = posts.get(0).getId();
            User another = userRepository.findByUsername("Another_test").orElseThrow();
            Authentication authentication = new UsernamePasswordAuthenticationToken(another.getUsername(), another.getPassword());
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            // when
            CustomException exception = assertThrows(CustomException.class,
                    () -> PostService.delete(postId));
            //then
            assertEquals(exception.getErrorCode(), ErrorCode.BAD_AUTH_DELETE);
        }
    }
}
package com.sparta.ottoon.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.ottoon.auth.config.SecurityConfig;
import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.entity.UserStatus;
import com.sparta.ottoon.auth.filter.JwtAuthenticationFilter;
import com.sparta.ottoon.auth.filter.JwtAuthorizationFilter;
import com.sparta.ottoon.auth.repository.UserRepository;
import com.sparta.ottoon.fixtureMonkey.FixtureMonkeyUtil;
import com.sparta.ottoon.mock.MockSpringSecurityFilter;
import com.sparta.ottoon.post.dto.PostRequestDto;
import com.sparta.ottoon.post.repository.PostRepository;
import com.sparta.ottoon.post.service.PostService;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("게시글 컨트롤러 테스트")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(
        controllers = {PostController.class},
        // 제외 필터 지정
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                ),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtAuthenticationFilter.class
                ),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtAuthorizationFilter.class
                )
        }
)
class PostControllerTest {
    private MockMvc mvc;
    private Principal principal;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    PostService postService;

    @MockBean
    PostRepository postRepository;

    @MockBean
    UserRepository userRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();

        mockUserSetup();
    }

    private void mockUserSetup() {
        User user = new User("testUsername", "testNickname", "testPassword", "test@email.com", UserStatus.ACTIVE);
        org.springframework.security.core.userdetails.User testUser = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
        principal = new UsernamePasswordAuthenticationToken(testUser, "", testUser.getAuthorities());
    }

    @Order(1)
    @DisplayName("게시물 생성")
    @Test
    void test1() throws Exception {
        // given
        PostRequestDto requestDto = FixtureMonkeyUtil.monkey()
                .giveMeBuilder(PostRequestDto.class)
                .set("contents", Arbitraries.strings().ofMinLength(1))
                .sample();

        String requestJson = objectMapper.writeValueAsString(requestDto);

        // when - then
        mvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .principal(principal)
        ).andExpect(status().is2xxSuccessful());
    }

    @Order(2)
    @DisplayName("게시물 전체 조회")
    @Test
    void test2() throws Exception {
        // given
        // when - then
        mvc.perform(get("/api/posts")
        ).andExpect(status().is2xxSuccessful());
    }

    @Order(3)
    @DisplayName("게시물 수정")
    @Test
    void test3() throws Exception {
        // given
        Long postId = 1L;

        PostRequestDto requestDto = FixtureMonkeyUtil.monkey()
                .giveMeBuilder(PostRequestDto.class)
                .set("content", Arbitraries.strings().ofMinLength(1))
                .sample();

        String requestJson = objectMapper.writeValueAsString(requestDto);

        // when - then
        mvc.perform(put("/api/posts/" + postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .principal(principal)
        ).andExpect(status().is2xxSuccessful());
    }

    @Order(4)
    @DisplayName("게시물 선택조회")
    @Test
    void test5() throws Exception {
        // given
        Long postId = 1L;

        // when - then
        mvc.perform(get("/api/posts/" + postId)
        ).andExpect(status().is2xxSuccessful());
    }

    @Order(5)
    @DisplayName("게시물 삭제")
    @Test
    void test4() throws Exception {
        // given
        Long postId = 1L;

        // when - then
        mvc.perform(delete("/api/posts/" + postId)
                .principal(principal)
        ).andExpect(status().is2xxSuccessful());
    }
}
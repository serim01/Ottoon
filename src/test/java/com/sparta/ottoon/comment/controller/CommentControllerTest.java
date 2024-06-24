package com.sparta.ottoon.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.ottoon.auth.config.SecurityConfig;
import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.entity.UserStatus;
import com.sparta.ottoon.auth.filter.JwtAuthenticationFilter;
import com.sparta.ottoon.auth.filter.JwtAuthorizationFilter;
import com.sparta.ottoon.comment.dto.CommentRequestDto;
import com.sparta.ottoon.comment.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(
        controllers = {CommentController.class},
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
@AutoConfigureMockMvc
public class CommentControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    Long postId = 1L;
    Long commentId = 1L;
    CommentRequestDto requestDto = new CommentRequestDto();

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();

        mockUserSetup();
    }

    private void mockUserSetup() {
        User user = new User("testUsername", "testNickname", "testPassword", "test@email.com", UserStatus.ACTIVE);
        org.springframework.security.core.userdetails.User testUser = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthorities());
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUser, "", testUser.getAuthorities());
    }

    @Test
    @DisplayName("createComment() 테스트")
    void test1() throws Exception {
        // When - Then
        mvc.perform(MockMvcRequestBuilders.post("/api/post/{postId}/comment", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("getComment() 테스트")
    void test2() throws Exception {
        // When - Then
        mvc.perform(MockMvcRequestBuilders.get("/api/post/{postId}/comment", postId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("updateComment() 테스트")
    void test3() throws Exception {
        // When - Then
        mvc.perform(MockMvcRequestBuilders.put("/api/post/{postId}/comment/{commentId}", postId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("deleteComment() 테스트")
    void test4() throws Exception {
        // When - Then
        mvc.perform(MockMvcRequestBuilders.delete("/api/post/{postId}/comment/{commentId}", postId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
    }
}

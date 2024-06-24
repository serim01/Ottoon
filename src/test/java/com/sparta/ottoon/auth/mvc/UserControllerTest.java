package com.sparta.ottoon.auth.mvc;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.ottoon.auth.config.SecurityConfig;
import com.sparta.ottoon.auth.controller.UserController;
import com.sparta.ottoon.auth.dto.LoginRequestDto;
import com.sparta.ottoon.auth.dto.SignupRequestDto;
import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.entity.UserStatus;
import com.sparta.ottoon.auth.filter.JwtAuthenticationFilter;
import com.sparta.ottoon.auth.filter.JwtAuthorizationFilter;
import com.sparta.ottoon.auth.service.SocialService;
import com.sparta.ottoon.auth.service.UserService;
import com.sparta.ottoon.mock.MockSpringSecurityFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(
        controllers = {UserController.class},
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

public class UserControllerTest {
    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private SocialService socialService;

    @Spy
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    void mockUserSetup() {

        Long id = 1L;
        String testUsername = "test1234";
        String testPassword = "test1234";
        String testNickname = "test";
        String testEmail = "test@test.test";
        String testIntroduce = "It's a test";
        UserStatus testUserStatus = UserStatus.ACTIVE;
        User testUser = new User(testUsername, testPassword, testNickname, testEmail, testUserStatus);
        org.springframework.security.core.userdetails.User testUserDetails = new org.springframework.security.core.userdetails.User(testUser.getUsername(), testUser.getPassword(), testUser.getAuthorities());
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @Test
    @DisplayName("회원가입")
    void test1() throws Exception {

        String testUsername = "test1234";
        String testPassword = "test1234";
        String testNickname = "test";
        String testEmail = "test@test.test";
        UserStatus testUserStatus = UserStatus.ACTIVE;
        String testAdminToken = "";
        boolean admin = false;
        SignupRequestDto signupRequestDto = new SignupRequestDto(
                testUsername,
                testPassword,
                testNickname,
                testEmail,
                testAdminToken,
                admin);

        String postInfo = objectMapper.writeValueAsString(signupRequestDto);

        mvc.perform(post("/api/auth/signup")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인")
    void test2() throws Exception {

        String testUsername = "test1234";
        String testPassword = "test1234";
        LoginRequestDto loginRequestDto = new LoginRequestDto(testUsername, testPassword);

        String postInfo = objectMapper.writeValueAsString(loginRequestDto);

        mvc.perform(post("/api/auth/login")
                        .content(postInfo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그아웃")
    void test3() throws Exception {

        mockUserSetup();

        mvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .principal(mockPrincipal)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

    }

}




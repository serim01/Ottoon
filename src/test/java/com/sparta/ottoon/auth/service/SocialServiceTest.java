package com.sparta.ottoon.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.ottoon.auth.dto.KakaoUserInfoDto;
import com.sparta.ottoon.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestTemplate;

import static com.sparta.ottoon.auth.jwt.JwtUtil.AUTHORIZATION_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

//
//@WebMvcTest(
//        controllers = {UserController.class},
//        excludeFilters = {
//                @ComponentScan.Filter(
//                        type = FilterType.ASSIGNABLE_TYPE,
//                        classes = SecurityConfig.class
//                ),
//                @ComponentScan.Filter(
//                        type = FilterType.ASSIGNABLE_TYPE,
//                        classes = JwtAuthenticationFilter.class
//                ),
//                @ComponentScan.Filter(
//                        type = FilterType.ASSIGNABLE_TYPE,
//                        classes = JwtAuthorizationFilter.class
//                )
//        }
//)


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 서버의 PORT 를 랜덤으로 설정합니다.
class SocialServiceTest {
    @Autowired
    private  RestTemplate restTemplate;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  PasswordEncoder passwordEncoder;

    String kakaoRedirectUri = "http://localhost:8888/api/auth/kakao";
    String kakaoTokenRequestUri = "https://kauth.kakao.com/oauth/token";
    String kakaoUserInfoRequestUri= "https://kapi.kakao.com/v2/user/me";
    private MockRestServiceServer mockServer;
    private SocialService socialService;

    @BeforeEach
    void setUp() {
        socialService = new SocialService(restTemplate, userRepository, passwordEncoder);
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void kakaoLogin() throws JsonProcessingException {
        String accessToken = "access token";
        Long kakaoId = 12345L;

        String response = String.format("{\"id\":\"%d\"}", kakaoId);

        mockServer.expect(requestTo(kakaoUserInfoRequestUri))
                .andExpect(content().contentType("application/x-www-form-urlencoded"))
//                .andExpect(method(HttpMethod.GET))
                .andExpect(MockRestRequestMatchers.header(AUTHORIZATION_HEADER, "bearer " + accessToken))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));

        KakaoUserInfoDto kakaoInfoResponse = socialService.getKakaoUserInfo(accessToken);

        assertThat(kakaoInfoResponse.getId()).isEqualTo(kakaoId);
    }

    @Test
    void registerKakaoUserIfNeeded() {
    }
}
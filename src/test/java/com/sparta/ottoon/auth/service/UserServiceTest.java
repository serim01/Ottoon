package com.sparta.ottoon.auth.service;

import com.sparta.ottoon.auth.dto.SignupRequestDto;
import com.sparta.ottoon.auth.entity.PasswordLog;
import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.entity.UserStatus;
import com.sparta.ottoon.auth.repository.PasswordLogRepository;
import com.sparta.ottoon.auth.repository.UserRepository;
import com.sparta.ottoon.common.exception.CustomException;
import com.sparta.ottoon.common.exception.ErrorCode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordLogRepository passwordLogRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;
    private User testUser;

    @BeforeEach
    void setUp() {
        userService = new UserService(
                userRepository,
                passwordLogRepository,
                passwordEncoder);
        testUser = new User(
                "testUsername",
                "testNickname",
                "testPassword",
                "testEmail",
                UserStatus.ACTIVE);
        ReflectionTestUtils.setField(userService, "ADMIN_TOKEN", "asdasd");
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class SignupTest {
        @Test
        @DisplayName("올바른 입력")
        void test1() {

            String username = "requestUsername";
            String nickname = "requestNickname";
            String password = "requestPassword";
            String email = "testEmail";
            String adminToken = "";
            boolean admin = false;

            SignupRequestDto signupRequestDto = new SignupRequestDto(
                    username,
                    nickname,
                    password,
                    email,
                    adminToken,
                    admin
            );

            userService.signup(signupRequestDto);

            verify(userRepository, times(1)).save(any(User.class));
            verify(passwordLogRepository, times(1)).save(any(PasswordLog.class));
        }

        @Test
        @DisplayName("중복된 이름")
        void test3() {

            String username = "requestUsername";
            String nickname = "requestNickname";
            String password = "requestPassword";
            String email = "testEmail";
            String adminToken = "";
            boolean admin = false;

            SignupRequestDto signupRequestDto = new SignupRequestDto(
                    username,
                    nickname,
                    password,
                    email,
                    adminToken,
                    admin
            );
            given(userRepository.findByUsername(anyString())).willReturn(Optional.of(testUser));

            Exception exception = assertThrows(CustomException.class,
                    () -> userService.signup(signupRequestDto));

            assertEquals("중복된 사용자가 존재합니다.", exception.getMessage());
        }

        @Test
        @DisplayName("중복된 이메일")
        void test2() {

            String username = "requestUsername";
            String nickname = "requestNickname";
            String password = "requestPassword";
            String email = "testEmail";
            String adminToken = "";
            boolean admin = false;

            SignupRequestDto signupRequestDto = new SignupRequestDto(
                    username,
                    nickname,
                    password,
                    email,
                    adminToken,
                    admin
            );
            given(userRepository.findByEmail(anyString())).willReturn(Optional.of(testUser));

            Exception exception = assertThrows(CustomException.class,
                    () -> userService.signup(signupRequestDto));

            assertEquals("중복된 이메일이 존재합니다.", exception.getMessage());
        }


        @Test
        @DisplayName("잘못된 어드민 토큰")
        void test4() {

            String username = "requestUsername";
            String nickname = "requestNickname";
            String password = "requestPassword";
            String email = "testEmail";
            String adminToken = "aasdasd";
            boolean admin = true;

            SignupRequestDto signupRequestDto = new SignupRequestDto(
                    username,
                    nickname,
                    password,
                    email,
                    adminToken,
                    admin
            );

            Exception exception = assertThrows(CustomException.class,
                    () -> userService.signup(signupRequestDto));

            assertEquals("관리자 암호가 일치하지 않습니다.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("로그아웃 테스트")
    class logoutTest {

        @Test
        @DisplayName("올바른 입력")
        void test1() {
            //given
            String username = testUser.getUsername();
            testUser.updateRefresh("testRefreshToken");
            given(userRepository.findByUsername(anyString())).willReturn(Optional.of(testUser));


            //when
            userService.logout(username);


            //then
            assertThat(testUser.getRefreshToken()).isNull();
            verify(userRepository, times(1)).save(any(User.class));
        }

        @Test
        @DisplayName("잘못된 유저")
        void test2() {
            //given
            String username = testUser.getUsername();
            given(userRepository.findByUsername(anyString())).willReturn(Optional.empty());

            //when
            Exception exception = assertThrows(CustomException.class, () -> userService.logout(username));


            //then
            assertEquals(ErrorCode.USER_NOT_FOUND.getMsg(), exception.getMessage());

        }

    }

}
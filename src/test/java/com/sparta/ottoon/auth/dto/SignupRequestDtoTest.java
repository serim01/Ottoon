package com.sparta.ottoon.auth.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SignupRequestDtoTest {
    private Validator validatorInjected;

    @BeforeEach
    void setUp() {
        validatorInjected = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("SignupRequestDto 생성 테스트 : 올바른 입력")
    void test1() {

        String username = "test1234";
        String nickname = "test";
        String email = "test@test.test";
        String password = "Test1234!";
        String adminToken = "";
        boolean admin = false;
        SignupRequestDto signupRequestDto = new SignupRequestDto(
                username,
                nickname,
                email,
                password,
                adminToken,
                admin);


        Set<ConstraintViolation<SignupRequestDto>> violations = validatorInjected.validate(signupRequestDto);

        assertThat(violations).isEmpty();

    }

    @Test
    @DisplayName("SignupRequestDto 생성 테스트 : username 규칙 위반")
    void test2() {
        String username = "tes";
        String nickname = "test";
        String email = "test@test.test";
        String password = "Test1234!";
        String adminToken = "";
        boolean admin = false;
        SignupRequestDto signupRequestDto = new SignupRequestDto(
                username,
                nickname,
                email,
                password,
                adminToken,
                admin);

        Set<ConstraintViolation<SignupRequestDto>> violations = validatorInjected.validate(signupRequestDto);

        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("아이디는 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 이루어져 있어야 합니다.");

    }

    @Test
    @DisplayName("SignupRequestDto 생성 테스트 : email 규칙 위반")
    void test3() {
        //given
        String username = "test1234";
        String nickname = "test";
        String email = "testtest.test";
        String password = "Test1234!";
        String adminToken = "";
        boolean admin = false;
        SignupRequestDto signupRequestDto = new SignupRequestDto(
                username,
                nickname,
                email,
                password,
                adminToken,
                admin);

        Set<ConstraintViolation<SignupRequestDto>> violations = validatorInjected.validate(signupRequestDto);

        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("이메일 형식이 올바르지 않습니다.");

    }

    @Test
    @DisplayName("SignupRequestDto 생성 테스트 : password 규칙 위반")
    void test4() {
        //given
        String username = "test1234";
        String nickname = "test";
        String email = "test@test.test";
        String password = "Test1234";
        String adminToken = "";
        boolean admin = false;
        SignupRequestDto signupRequestDto = new SignupRequestDto(
                username,
                nickname,
                email,
                password,
                adminToken,
                admin);

        Set<ConstraintViolation<SignupRequestDto>> violations = validatorInjected.validate(signupRequestDto);

        assertThat(violations).hasSize(1);
        assertThat(violations)
                .extracting("message")
                .contains("비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 이루어져 있어야 합니다.");

    }

    @Test
    @DisplayName("SignupRequestDto 생성 테스트 :  규칙 일체 위반")
    void test5() {

        String username = "tes";
        String nickname = "test";
        String email = "testtest.test";
        String password = "Test1234";
        String adminToken = "";
        boolean admin = false;
        SignupRequestDto signupRequestDto = new SignupRequestDto(
                username,
                nickname,
                email,
                password,
                adminToken,
                admin);

        Set<ConstraintViolation<SignupRequestDto>> violations = validatorInjected.validate(signupRequestDto);

        assertThat(violations).hasSize(3);
        assertThat(violations)
                .extracting("message")
                .contains("비밀번호는 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 이루어져 있어야 합니다.")
                .contains("이메일 형식이 올바르지 않습니다.")
                .contains("아이디는 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 이루어져 있어야 합니다.");

    }

}
package com.sparta.ottoon.backoffice;

import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.entity.UserStatus;
import com.sparta.ottoon.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BackOfficeServiceTest {
    @Mock
    private UserRepository userRepository;

    private BackOfficeService backOfficeService;

    @BeforeEach
    void setUp() {
        backOfficeService = new BackOfficeService(userRepository);
    }

    @Test
    @DisplayName("enum valid test")
    void test() {
        //given
        User user = new User("test", "test", "test", UserStatus.ACTIVE);
        given(userRepository.findByUsername(anyString())).willReturn(Optional.of(user));
        String username = "Test";

        EditRequestDto editRequestDto = new EditRequestDto("ADMIN");

        //when
        UserResponseDto result = backOfficeService.editUserRole(username, editRequestDto);

        //then
        assertEquals(UserStatus.ADMIN, result.getUserStatus());

    }

}
package com.sparta.ottoon.backoffice;

import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserResponseDto {
    private UserStatus userStatus;

    public UserResponseDto(User user) {
        this.userStatus=user.getStatus();

    }
}

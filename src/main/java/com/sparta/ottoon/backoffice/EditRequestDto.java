package com.sparta.ottoon.backoffice;

import com.sparta.ottoon.auth.entity.UserStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EditRequestDto {
    @Enum(enumclass = UserStatus.class, ignoreCase = true, message = "enum : 잘못된 입력입니다.")
    private String userStatus;
}

package com.sparta.ottoon.backoffice.dto;

import com.sparta.ottoon.auth.entity.UserStatus;
import com.sparta.ottoon.common.customvalidation.Enum;
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

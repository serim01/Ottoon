package com.sparta.ottoon.profile.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProfileRequestDto {
    private String nickname;
    private String intro;

    public ProfileRequestDto(String nickname, String intro){
        this.nickname = nickname;
        this.intro = intro;
    }
}

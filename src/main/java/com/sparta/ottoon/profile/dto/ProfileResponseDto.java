package com.sparta.ottoon.profile.dto;

import com.sparta.ottoon.auth.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProfileResponseDto {
    private String username;
    private String email;
    private String nickname;
    private String intro;
    private String status;
    private Integer commentLikes;
    private Integer postLikes;

    public ProfileResponseDto(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.intro = user.getIntro();
        this.status = user.getStatus().getStatus();
    }

    public ProfileResponseDto(User user, Integer postLikes, Integer commentLikes) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.intro = user.getIntro();
        this.status = user.getStatus().getStatus();
        this.postLikes = postLikes;
        this.commentLikes = commentLikes;
    }
}

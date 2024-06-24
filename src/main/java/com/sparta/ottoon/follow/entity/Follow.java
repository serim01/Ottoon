package com.sparta.ottoon.follow.entity;

import com.sparta.ottoon.auth.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "follow")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "follow_check", nullable = false)
    boolean isFollow;

    @Column(name = "follow_user_id",nullable = false)
    long userId;

    @ManyToOne
    @JoinColumn(name = "followed_user_id")
    User followUser;

    public Follow(boolean isFollow, long userId, User followUser) {
        this.isFollow = isFollow;
        this.userId = userId;
        this.followUser = followUser;
    }

    public void changeFollow(boolean isFollow){
        this.isFollow = isFollow;
    }
}

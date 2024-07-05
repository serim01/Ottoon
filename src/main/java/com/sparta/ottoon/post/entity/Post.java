package com.sparta.ottoon.post.entity;

import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.comment.entity.Comment;
import com.sparta.ottoon.common.Timestamped;
import com.sparta.ottoon.like.entity.Like;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PostStatus postStatus;

    @Column(name = "is_top",nullable = false)
    private boolean isTop;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    public Post(String contents) {
        this.contents = contents;
        this.postStatus = PostStatus.GENERAL;
    }

    @Builder
    public Post(String contents, User user) {
        this.contents = contents;
        this.postStatus = PostStatus.GENERAL;
        this.user = user;
    }

    public void update(String contents) {
        this.contents = contents;
    }

    public void noticed() {
        if (this.getPostStatus().equals(PostStatus.NOTICE)) {
            this.postStatus = PostStatus.GENERAL;
            this.isTop = false;
        } else {
            this.postStatus = PostStatus.NOTICE;
            this.isTop = true;
        }
    }

    // Boolean 필드에 대한 getter는 'is'로 시작해야 함
    public boolean isTop() {
        return isTop;
    }
}
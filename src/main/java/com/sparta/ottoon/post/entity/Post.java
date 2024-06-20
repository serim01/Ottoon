package com.sparta.ottoon.post.entity;

import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.common.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "posts")
@Setter
@Getter
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
    @Column(nullable = false)
    private boolean is_top;

}
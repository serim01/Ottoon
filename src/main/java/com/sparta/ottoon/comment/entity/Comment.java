package com.sparta.ottoon.comment.entity;

import com.sparta.ottoon.common.Timestamped;
import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.entity.Post;
import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;
}

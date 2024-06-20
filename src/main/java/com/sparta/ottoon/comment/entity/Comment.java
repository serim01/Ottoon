package com.sparta.ottoon.comment.entity;

import com.sparta.ottoon.common.Timestamped;
import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comments")
@NoArgsConstructor
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

    public Comment(Long commentId, String comment, User user, Post post) {
        this.commentId = commentId;
        this.comment = comment;
        this.user = user;
        this.post = post;
    }

}

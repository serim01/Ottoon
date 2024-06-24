package com.sparta.ottoon.like.entity;

import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.comment.entity.Comment;
import com.sparta.ottoon.common.Timestamped;
import com.sparta.ottoon.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "like")
@NoArgsConstructor
public class Like extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Column(name = "like_type", nullable = false)
    private LikeTypeEnum liketype;



    public Like(User user, Post post, Comment comment, LikeTypeEnum liketype) {
        this.user = user;
        this.post = post;
        this.comment = comment;
        this.liketype = liketype;
    }
}

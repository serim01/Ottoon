package com.sparta.ottoon.like.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ottoon.comment.dto.CommentResponseDto;
import com.sparta.ottoon.comment.dto.QCommentResponseDto;
import com.sparta.ottoon.comment.entity.QComment;
import com.sparta.ottoon.like.entity.QLike;
import com.sparta.ottoon.post.dto.PostResponseDto;
import com.sparta.ottoon.post.dto.QPostResponseDto;
import com.sparta.ottoon.post.entity.QPost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PostResponseDto> findLikedPostsByUserId(Long userId, Pageable pageable) {
        QLike like = QLike.like;
        QPost post = QPost.post;
        QLike myLike = new QLike("myLike");

        List<PostResponseDto> content = jpaQueryFactory
                .select(new QPostResponseDto(
                        post.id.as("postId"),
                        post.contents,
                        like.id.count().intValue(),
                        post.user.nickname.as("nickname"),
                        post.createdAt,
                        post.modifiedAt
                ))
                .from(post)
                .leftJoin(like).on(post.id.eq(like.post.id))
                .leftJoin(myLike).on(myLike.post.id.eq(post.id).and(myLike.user.id.eq(userId)))
                .groupBy(post.id)
                .orderBy(createOrderSpecifier(post.createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(like.post.id.countDistinct())
                .from(like)
                .where(like.user.id.eq(userId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<CommentResponseDto> findLikedCommentsByUserId(Long userId, Pageable pageable) {
        QLike like = QLike.like;
        QComment comment = QComment.comment1;
        QLike myLike = new QLike("myLike");

        List<CommentResponseDto> content = jpaQueryFactory
                .select(new QCommentResponseDto(
                        comment.id.as("commentId"),
                        comment.comment,
                        comment.user.nickname.as("nickname"),
                        like.id.count().intValue(),
                        comment.createdAt,
                        comment.modifiedAt
                ))
                .from(comment)
                .leftJoin(like).on(comment.id.eq(like.post.id))
                .leftJoin(myLike).on(myLike.post.id.eq(comment.id).and(myLike.user.id.eq(userId)))
                .groupBy(comment.id)
                .orderBy(createOrderSpecifier(comment.createdAt))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(like.comment.id.countDistinct())
                .from(like)
                .where(like.user.id.eq(userId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    private OrderSpecifier<?> createOrderSpecifier(DateTimePath<LocalDateTime> createdAtPath) {
        return new OrderSpecifier<>(Order.DESC, createdAtPath);
    }
}

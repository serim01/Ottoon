package com.sparta.ottoon.profile.repository;

import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.sparta.ottoon.like.entity.QLike.like;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    NumberExpression<Integer> count = like.count().intValue();

    @Override
    public Integer getLikePostCountById(Long userId){
        return queryFactory
                .select(count)
                .from(like)
                .where(like.user.id.eq(userId)
                        .and(like.post.isNotNull()))
                .fetchOne();
    }

    @Override
    public Integer getLikeCommentCountById(Long userId){
        return queryFactory
                .select(count)
                .from(like)
                .where(like.user.id.eq(userId)
                        .and(like.comment.isNotNull()))
                .fetchOne();
    }
}

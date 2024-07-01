package com.sparta.ottoon.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ottoon.post.dto.PostResponseDto;
import com.sparta.ottoon.post.dto.QPostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.sparta.ottoon.like.entity.QLike.like;
import static com.sparta.ottoon.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<PostResponseDto> findWithLikeCountById(Long id) {

        return Optional.ofNullable(queryFactory
                .select(new QPostResponseDto(
                        post.id,
                        post.contents,
                        like.count().intValue(),
                        post.user.nickname,
                        post.createdAt,
                        post.modifiedAt
                ))
                .from(post)
                .leftJoin(post.likes, like)
                .where(post.id.eq(id))
                .groupBy(post.id)
                .fetchOne()
        );

    }
}

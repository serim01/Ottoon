package com.sparta.ottoon.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ottoon.comment.dto.CommentResponseDto;
import com.sparta.ottoon.comment.dto.QCommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.sparta.ottoon.comment.entity.QComment.comment1;
import static com.sparta.ottoon.like.entity.QLike.like;

@Repository
@RequiredArgsConstructor
public class CustomCommentRepositoryImpl implements CustomCommentRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<CommentResponseDto> findWithLikeCountById(Long id) {

        return Optional.ofNullable(queryFactory
                .select(new QCommentResponseDto(
                        comment1.id,
                        comment1.comment,
                        comment1.user.nickname,
                        like.count().intValue(),
                        comment1.createdAt,
                        comment1.modifiedAt
                ))
                .from(comment1)
                .leftJoin(comment1.likes, like)
                .where(comment1.id.eq(id))
                .groupBy(comment1.id)
                .fetchOne()
        );

    }
}

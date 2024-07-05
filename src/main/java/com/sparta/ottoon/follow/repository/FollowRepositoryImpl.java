package com.sparta.ottoon.follow.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ottoon.follow.entity.QFollow;
import com.sparta.ottoon.post.entity.Post;
import com.sparta.ottoon.post.entity.QPost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public List<Post> findAllFollowPostList(Long userId, Pageable pageable) {
        QFollow follow = QFollow.follow;
        QPost post = QPost.post;

        return queryFactory.selectFrom(post)
                .innerJoin(follow).on(post.user.id.eq(follow.followUser.id))
                .where(follow.userId.eq(userId))
                .orderBy(post.isTop.desc(), post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

    }
}

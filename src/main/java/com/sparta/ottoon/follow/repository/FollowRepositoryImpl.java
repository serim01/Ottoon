package com.sparta.ottoon.follow.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.ottoon.follow.entity.QFollow;
import com.sparta.ottoon.post.entity.Post;
import com.sparta.ottoon.post.entity.QPost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sparta.ottoon.post.entity.QPost.post;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryImpl implements FollowRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public List<Post> findAllFollowPostList(Long userId, Pageable pageable) {
        QFollow follow = QFollow.follow;
        QPost post = QPost.post;

        String sort = pageable.getSort().toString();
        int colonIndex = sort.indexOf(":");
        String sortType = sort.substring(0, colonIndex).trim();

        // 정렬 조건을 생성
        List<OrderSpecifier<?>> orderSpecifiers = createOrderSpecifier(sortType);

        return queryFactory.selectFrom(post)
                .innerJoin(follow).on(post.user.id.eq(follow.followUser.id))
                .where(follow.userId.eq(userId))
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

    }

    private List<OrderSpecifier<?>> createOrderSpecifier(String sortType) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, post.isTop));

        if(Objects.equals(sortType, "writer")){
            orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, post.user.nickname));
        }else{
            orderSpecifiers.add(new OrderSpecifier<>(Order.DESC, post.createdAt));
        }

        return orderSpecifiers;
    }
}

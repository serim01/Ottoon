package com.sparta.ottoon.follow.repository;

import com.sparta.ottoon.post.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FollowRepositoryCustom {

    List<Post> findAllFollowPostList(Long userId, Pageable pageable);
}

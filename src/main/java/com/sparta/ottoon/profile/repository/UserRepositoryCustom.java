package com.sparta.ottoon.profile.repository;

public interface UserRepositoryCustom {
    Integer getLikePostCountById(Long id);
    Integer getLikeCommentCountById(Long id);
}

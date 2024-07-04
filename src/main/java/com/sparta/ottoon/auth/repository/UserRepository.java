package com.sparta.ottoon.auth.repository;

import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.profile.repository.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    Optional<User> findByKakaoId(Long kakaoId);
}

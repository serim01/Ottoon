package com.sparta.ottoon.post.repository;

import com.sparta.ottoon.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Page<Post> findAllByOrderByIsTopDescCreatedAtDesc(Pageable pageable);
}

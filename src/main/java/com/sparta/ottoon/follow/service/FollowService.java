package com.sparta.ottoon.follow.service;

import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.service.UserService;
import com.sparta.ottoon.common.exception.CustomException;
import com.sparta.ottoon.common.exception.ErrorCode;
import com.sparta.ottoon.follow.entity.Follow;
import com.sparta.ottoon.follow.repository.FollowRepository;
import com.sparta.ottoon.post.dto.PostResponseDto;
import com.sparta.ottoon.post.entity.Post;
import com.sparta.ottoon.profile.dto.ProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    @Transactional
    public ProfileResponseDto followUser(long followId, User user) {
        if(followId == user.getId()){
            throw new CustomException(ErrorCode.NOT_FOLLOW_SELF);
        }
        User followUser = userService.findById(followId);
        Follow newFollow = new Follow(true, user.getId(), followUser);

        if (!isAlreadyFollow(followId, user)) {
            followRepository.save(newFollow);
        }

        return new ProfileResponseDto(followUser);
    }

    @Transactional
    public ProfileResponseDto followCancel(long followId, User user) {
        User followUser = userService.findById(followId);
        Follow cancelFollow = followRepository.findByFollowUserAndUserId(followUser, user.getId()).orElseThrow(() ->
                new CustomException(ErrorCode.FAIL_FIND_USER));

        if (cancelFollow.isFollow()) {
            cancelFollow.changeFollow(false);
        } else {
            throw new CustomException(ErrorCode.NOT_FOLLOW);
        }

        return new ProfileResponseDto(cancelFollow.getFollowUser());
    }

    @Transactional(readOnly = true)
    public List<Follow> getFollowList(long userId) {
        return followRepository.findAllByFollowUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<PostResponseDto> followGetPost(Pageable pageable, User user) {
        List<Post> postList = followRepository.findAllFollowPostList(user.getId(), pageable);
        if (postList.isEmpty()) {
            throw new CustomException(ErrorCode.POST_EMPTY);
        }
        return postList.stream()
                .map(PostResponseDto::new)
                .toList();
    }

    private boolean isAlreadyFollow(long followId, User user) {
        User followUser = userService.findById(followId);
        Optional<Follow> curFollow = followRepository.findByFollowUserAndUserId(followUser, user.getId());
        if (curFollow.isPresent()) {
            if (curFollow.get().isFollow()) {
                throw new CustomException(ErrorCode.BAD_FOLLOW);
            } else {
                curFollow.get().changeFollow(true);
                return true;
            }
        }
        return false;
    }
}

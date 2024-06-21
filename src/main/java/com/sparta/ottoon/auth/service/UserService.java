package com.sparta.ottoon.auth.service;

import com.sparta.ottoon.auth.dto.SignupRequestDto;
import com.sparta.ottoon.auth.entity.PasswordLog;
import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.entity.UserStatus;
import com.sparta.ottoon.auth.repository.PasswordLogRepository;
import com.sparta.ottoon.auth.repository.UserRepository;
import com.sparta.ottoon.common.exception.CustomException;
import com.sparta.ottoon.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.ErrorState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    final private UserRepository userRepository;
    final private PasswordLogRepository passwordLogRepository;
    final private PasswordEncoder passwordEncoder;

    @Value("${ADMIN_TOKEN}")
    private String ADMIN_TOKEN;

    /**
     * 회원가입
     * @param requestDto
     */
    @Transactional
    public String signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String nickname = requestDto.getNickname();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_UESR);
        }

        user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        // 관리자 권한 검사
        UserStatus status = UserStatus.ACTIVE;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new CustomException(ErrorCode.INCORRECT_ADMIN);
            }
            status = UserStatus.ADMIN;
        }

        // 회원가입한 user DB에 저장
        User saveUser = userRepository.save(new User(username, nickname, password, email, status));

        // 비밀번호 로그 DB에 저장
        passwordLogRepository.save(new PasswordLog(password, saveUser));

        return "회원가입에 성공하였습니다.";
    }

    /**
     * refresh token을 없앰으로써 로그아웃
     * @param username
     */
    @Transactional
    public String logout(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        // user의 refresh token을 없앤다.
        user.clearRefreshToken();
        userRepository.save(user);

        return "로그아웃 하였습니다.";
    }
}

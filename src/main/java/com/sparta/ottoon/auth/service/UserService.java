package com.sparta.ottoon.auth.service;

import com.sparta.ottoon.auth.dto.SignupRequestDto;
import com.sparta.ottoon.auth.entity.PasswordLog;
import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.entity.UserStatus;
import com.sparta.ottoon.auth.repository.PasswordLogRepository;
import com.sparta.ottoon.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new IllegalArgumentException("중복된 이메일입니다.");
        }

        // 관리자 권한 검사
        UserStatus status = UserStatus.ACTIVE;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀립니다.");
            }
            status = UserStatus.ADMIN;
        }

        // 회원가입한 user DB에 저장
        User saveUser = userRepository.save(new User(username, password, email, status));

        // 비밀번호 로그 DB에 저장
        passwordLogRepository.save(new PasswordLog(password, saveUser));
    }

    /**
     * refresh token을 없앰으로써 로그아웃
     * @param username
     */
    @Transactional
    public void logout(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("유저를 찾을 수 없습니다.")
        );

        // user의 refresh token을 없앤다.
        user.clearRefreshToken();
        userRepository.save(user);
    }
}

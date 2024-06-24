package com.sparta.ottoon.auth.filter;

import com.sparta.ottoon.auth.entity.TokenError;
import com.sparta.ottoon.auth.entity.User;
import com.sparta.ottoon.auth.jwt.JwtUtil;
import com.sparta.ottoon.auth.repository.UserRepository;
import com.sparta.ottoon.auth.service.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j(topic = "JwtAuthorizationFilter")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    /**
     * JWT 토큰을 검증해서 유효하면 사용자 정보 설정
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 jwt token을 가져온다.
        String token = jwtUtil.getJwtTokenFromHeader(request);



        if (StringUtils.hasText(token)) { // token의 값이 null이나 빈칸이 아닐 경우

            // token의 유효성 검사
            checkTokenzAndErrorHanding(response, token);

            // token에서 사용자의 정보를 가져온다.
            Claims userInfo = jwtUtil.getUserInfoFromToken(token);

            try {
                // username으로 인증객체 설정
                setAuthentication(userInfo.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        // 다음 필터로
        filterChain.doFilter(request, response);
    }

    /**
     * 인증 객체 저장
     * @param username
     */
    public void setAuthentication(String username) {
        // SecurityContext : 사용자의 보안 관련 정보를 저장하는 컨테이너
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // username을 기반으로 인증 객체 생성
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        // SecurityContexHolder : 이 곳에 저장되어 있는 객체로 보안 검사 및 접근 제어를 수행
        SecurityContextHolder.setContext(context);
    }

    /**
     * username으로 인증 객체 설정
     * @param username
     * @return
     */
    private Authentication createAuthentication(String username) {
        //UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        User user = userRepository.findByUsername(username).orElseThrow();
        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(user);
        return new UsernamePasswordAuthenticationToken(userDetailsImpl, null, user.getAuthorities());
    }


    /**
     * token의 유효성 처리와 에러 핸들링 : token 만료처리, 에러 처리
     * @param response
     * @param token
     * @throws IOException
     */
   private void checkTokenzAndErrorHanding(HttpServletResponse response, String token) throws IOException {
       switch (jwtUtil.validateToken(token)) {
           case VALID: break;
           case EXPRIED:
               // access token이 만료되었으므로 refresh token을 기반으로 처리해준다.
               updateAccessToken(response, token);
               return;
           default:
               response.setStatus(401);
               response.setContentType("text/plain;charset=UTF-8");
               response.getWriter().write("유효하지 않은 토큰입니다.");
               return;
       }

       checkBlacklist(token); // 무효한 토큰인지 검사
   }

    /**
     * refresh token을 기반으로 access token 재발급 또는 재로그인 유도
     * @param response
     * @param token
     * @throws IOException
     */
    private void updateAccessToken(HttpServletResponse response, String token) throws IOException {
        String username = jwtUtil.getUserInfoFromToken(token).getSubject();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );

        String refresh = user.getRefreshToken().substring(7);
        if (TokenError.VALID == jwtUtil.validateToken(refresh)) { // refresh token이 유효한 경우 access token을 재발급 해준다.
            String newToken = jwtUtil.createToken(user.getUsername(), JwtUtil.accessTokenExpiration);
            response.addHeader(JwtUtil.AUTHORIZATION_HEADER, newToken);
        } else if (TokenError.EXPRIED == jwtUtil.validateToken(refresh)){ // refresh token이 만료되어 클라이언트로 사용자의 재로그인을 유도한다.
            response.setStatus(401);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("refresh 토큰이 만료되었습니다.");
        } else {
            throw new RuntimeException("refresh token이 유효하지 않습니다.");
        }
    }

    /**
     * 로그아웃한 사용자인지 검사
     * @param token
     */
    private void checkBlacklist(String token) {
        String username = jwtUtil.getUserInfoFromToken(token).getSubject();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("해당 유저는 없습니다.")
        );

        // refresh token이 null이라면 로그아웃한 사용자
        if (Objects.isNull(user.getRefreshToken())) {
            throw new RuntimeException("무효한 토큰입니다.");
        }
    }
}
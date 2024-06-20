package com.sparta.ottoon.auth.config;

import com.sparta.ottoon.auth.filter.JwtAuthenticationFilter;
import com.sparta.ottoon.auth.filter.JwtAuthorizationFilter;
import com.sparta.ottoon.auth.jwt.JwtUtil;
import com.sparta.ottoon.auth.repository.UserRepository;
import com.sparta.ottoon.auth.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
/**
 * spring security 설정
 */
public class SecurityConfig {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserRepository userRepository;

    /**
     * passwordEncoder 빈으로 등록
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 해시 함수를 사용하여 비밀번호 암호화
    }

    /**
     * spring security에서 인증을 관리하는 매니저 빈으로 등록
     * @param configuration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * jwtAuthenticationFilter 빈으로 등록
     * @return
     * @throws Exception
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, userRepository);

        // setAuthenticationManager : JwtAuthenticationFilter에 사용할 AuthenticationManager 설정
        // authenticationManager : spring security에서 구성된 AuthenticationManager를 가져온다.
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    /**
     * jwtAuthorizationFilter 빈으로 등록
     * @return
     */
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService, userRepository);
    }

    /**
     * 보안 설정을 기반으로 SecurityFilterChain 설정
     * @param httpSecurity
     * @return : SecurityFilterChain 객체 생성 (http 요청의 보안 규칙이 적용된)
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // CSRF 비활성화
        httpSecurity.csrf((csrf) -> csrf.disable());

        // 세션 관리를 stateless로 설정
        httpSecurity.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 요청에 대한 권한 설정
        httpSecurity.authorizeHttpRequests((authorizeRequest) ->
                authorizeRequest
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/api/auth/**").permitAll() // 회원가입, 로그인을 위한 auth 경로 접근 허용
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**", "/swagger-resources/**").permitAll() // Swagger 접근 허용
                        .anyRequest().authenticated() // 그 외 모든 요청에 대해 인증 처리
        );

        // 필터의 위치 지정
        httpSecurity.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        httpSecurity.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}

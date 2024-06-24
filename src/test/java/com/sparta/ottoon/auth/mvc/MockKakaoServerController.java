package com.sparta.ottoon.auth.mvc;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockKakaoServerController {

// ...

//    @PostMapping(path = "/oauth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public ResponseEntity<KakaoAccessTokenResponse> getAccessToken(HttpEntity<String> request) {
//
//        // ...
//
//        KakaoAccessTokenResponse response = KakaoAccessTokenResponse.builder()
//                .accessToken("access token")
//                .build();
//
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping(path = "/user/me", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public ResponseEntity<KakaoAccessTokenResponse> getMemberInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
//
//        // ...
//
//        KakaoAccessTokenResponse response = KakaoAccessTokenResponse.builder()
//                .id(54321L)
//                .build();
//
//        return ResponseEntity.ok(response);
//    }
}
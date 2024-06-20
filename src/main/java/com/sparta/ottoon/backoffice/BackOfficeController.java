package com.sparta.ottoon.backoffice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class BackOfficeController {

    private final BackOfficeService backOfficeService;

    /*
    전체 회원 조회
     */
    @GetMapping("/users")
    public ResponseEntity
     return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fieldError.getDefaultMessage());

}

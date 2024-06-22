package com.sparta.ottoon.common.aop;

import com.sparta.ottoon.auth.dto.SignupRequestDto;
import com.sparta.ottoon.common.exception.CustomException;
import com.sparta.ottoon.common.exception.ErrorCode;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Aspect
@Component
public class ValidBindingResultAop {

    @Pointcut("execution(* com.sparta.ottoon.auth.controller.UserController.*(..))")
    private void auth() {}

    @Before("auth() && args(requestDto, bindingResult)")
    public void handleBindingResult(SignupRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                // 로깅 또는 원하는 처리 수행
                System.out.println("BindingResult Error: " + fieldError.getField() + " - " + fieldError.getDefaultMessage());
            }

            throw new CustomException(ErrorCode.FAIL);
        }
    }
}

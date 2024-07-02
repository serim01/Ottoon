package com.sparta.ottoon.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonResponseDto<T> {
    private String msg;
    private int statusCode;
    private T result;
}

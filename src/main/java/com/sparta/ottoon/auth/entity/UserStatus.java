package com.sparta.ottoon.auth.entity;

import lombok.Getter;

@Getter
public enum UserStatus {
   ACTIVE("정상"),
   WITHDRAW("탈퇴"),
   BLOCK("차단"),
   ADMIN("관리자");

   final private String status;

   UserStatus(String status) {
      this.status = status;
   }
}

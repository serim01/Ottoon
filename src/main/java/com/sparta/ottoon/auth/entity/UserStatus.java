package com.sparta.ottoon.auth.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

import lombok.Getter;

@Getter
public enum UserStatus {
   ACTIVE(Authority.ACTIVE),
   WITHDRAW(Authority.WITHDRAW),
   BLOCK(Authority.BLOCK),
   ADMIN(Authority.ADMIN),
   DELETE(Authority.DELETE);

   final private String status;

   UserStatus(String status) {
      this.status = status;
   }

   public String getStatus() {
      return status;
   }

   public static class Authority {
      public static final String ACTIVE = "ACTIVE";
      public static final String WITHDRAW = "WITHDRAW";
      public static final String BLOCK = "BLOCK";
      public static final String ADMIN = "ADMIN";
      public static final String DELETE = "DELETE";
   }

   @JsonCreator
   public static UserStatus parsing(String inputValue) {
      return Stream.of(UserStatus.values())
              .filter(UserStatus -> UserStatus.toString().equals(inputValue.toUpperCase()))
              .findFirst()
              .orElse(null);
   }

}

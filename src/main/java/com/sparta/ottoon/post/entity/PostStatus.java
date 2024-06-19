package com.sparta.ottoon.post.entity;

public enum PostStatus {
    NOTICE("공지"),
    GENERAL("일반");

    final private String status;

    PostStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

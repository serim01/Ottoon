package com.sparta.ottoon.auth.entity;

import com.sparta.ottoon.common.Timestamped;
import jakarta.persistence.*;

@Entity
@Table(name="user")
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, length = 50)
    private String username;
    @Column(nullable = false, length = 50)
    private String password;
    @Column(nullable = false, length = 50)
    private String email;
    @Column(length = 100)
    private String intro;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserStatus role;
    @Column(nullable = false, length = 100)
    private String refreshToken;
}
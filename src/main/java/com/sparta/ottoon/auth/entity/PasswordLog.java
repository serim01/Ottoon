package com.sparta.ottoon.auth.entity;

import com.sparta.ottoon.common.Timestamped;
import jakarta.persistence.*;

@Entity
@Table(name="passwordlog")
public class PasswordLog extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String passwordLog;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}

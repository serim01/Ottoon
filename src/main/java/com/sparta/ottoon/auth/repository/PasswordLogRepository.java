package com.sparta.ottoon.auth.repository;

import com.sparta.ottoon.auth.entity.PasswordLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordLogRepository extends JpaRepository<PasswordLog, Long> {
}

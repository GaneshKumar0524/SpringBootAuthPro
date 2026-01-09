package com.project.SpringBootAuthPro.repository;

import com.project.SpringBootAuthPro.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    void deleteByEmail(String email);
}

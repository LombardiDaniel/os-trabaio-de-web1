package com.aa2.GamePlatform.repositories;

import com.aa2.GamePlatform.models.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    UserSession findByToken(String token);
}
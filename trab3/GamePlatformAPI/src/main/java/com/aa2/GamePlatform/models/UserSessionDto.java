package com.aa2.GamePlatform.models;

import java.time.Instant;

public class UserSessionDto {
    private String token;
    private Integer testerId;
    private Instant createdAt;
    private Instant expiresAt;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getTesterId() {
        return testerId;
    }

    public void setTesterId(Integer testerId) {
        this.testerId = testerId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }
}

package com.aa2.GamePlatform.models;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

public class TesterDto {
    @NotEmpty(message = "Cannot empty, first name required")
    private String firstName;

    @NotEmpty(message = "Cannot empty, last name required")
    private String lastName;

    @NotEmpty(message = "Cannot empty, email required ")
    @Email
    private String email;

    @NotEmpty(message = "Cannot empty, setup if user is admin required")
    private Boolean isUserAdmin;
}

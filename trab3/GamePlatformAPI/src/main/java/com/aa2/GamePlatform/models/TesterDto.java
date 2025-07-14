package com.aa2.GamePlatform.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class TesterDto {
    @NotEmpty(message = "Cannot empty, first name required")
    private String firstName;

    @NotEmpty(message = "Cannot empty, last name required")
    private String lastName;

    @NotEmpty(message = "Cannot empty, email required ")
    @Email
    private String email;

    @NotNull(message = "Cannot null, setup if user is admin required")
    private Boolean isUserAdmin; // Field name

    @NotEmpty(message = "Cannot empty, password required")
    private String password;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsUserAdmin() {
        return isUserAdmin;
    }

    public void setIsUserAdmin(Boolean isUserAdmin) {
        this.isUserAdmin = isUserAdmin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
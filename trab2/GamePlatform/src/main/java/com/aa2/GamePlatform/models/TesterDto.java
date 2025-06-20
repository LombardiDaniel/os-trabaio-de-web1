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
    private Boolean isUserAdmin;

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

    public Boolean getUserAdmin() {
        return isUserAdmin;
    }

    public void setUserAdmin(Boolean userAdmin) {
        isUserAdmin = userAdmin;
    }
}

package com.aa2.GamePlatform.models;

public enum TestSessionStatus {
    CREATED("Session is Created"),
    IN_PROGRESS("Session is in Progress"),
    COMPLETED("Session is Completed");

    private final String description;

    // Enum constructor to associate a description with each status.
    TestSessionStatus(String description) {
        this.description = description;
    }

    // Getter for the status description.
    public String getDescription() {
        return description;
    }
}
package com.aa2.GamePlatform.models;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class BugDto {

    @NotEmpty(message = "Cannot be empty, Bug title is required.")
    private String title;

    @NotEmpty(message = "Cannot be empty, Bug description is required.")
    private String description;

    @NotEmpty(message = "Cannot be empty, Bug steps to reproduce is required.")
    private String stepsToReproduce;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Cannot be null, Bug status is required.") // MUST BE @NotNull
    private BugStatus status;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Cannot be null, Bug priority is required.") // MUST BE @NotNull
    private BugPriority priority;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Cannot be null, Bug severity is required.") // MUST BE @NotNull
    private BugSeverity severity;

    public BugDto(String title, String description, String stepsToReproduce, BugStatus status, BugPriority priority, BugSeverity severity) {
        this.title = title;
        this.description = description;
        this.stepsToReproduce = stepsToReproduce;
        this.status = status;
        this.priority = priority;
        this.severity = severity;
    }

    public BugDto() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStepsToReproduce() {
        return stepsToReproduce;
    }

    public void setStepsToReproduce(String stepsToReproduce) {
        this.stepsToReproduce = stepsToReproduce;
    }

    public BugStatus getStatus() {
        return status;
    }

    public void setStatus(BugStatus status) {
        this.status = status;
    }

    public BugPriority getPriority() {
        return priority;
    }

    public void setPriority(BugPriority priority) {
        this.priority = priority;
    }

    public BugSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(BugSeverity severity) {
        this.severity = severity;
    }
}

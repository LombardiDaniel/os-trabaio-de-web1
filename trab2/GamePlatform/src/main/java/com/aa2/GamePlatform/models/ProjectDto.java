package com.aa2.GamePlatform.models;

import jakarta.validation.constraints.NotEmpty;

public class ProjectDto {
    @NotEmpty(message = "Cannot empty, project name required")
    private String projectName;

    @NotEmpty(message = "Cannot empty, project description required")
    private String projectDescription;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }
}

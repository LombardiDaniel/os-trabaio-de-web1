package com.aa2.GamePlatform.models;

import jakarta.validation.constraints.NotEmpty;

public class StrategyDto {
    @NotEmpty(message = "Cannot be empty, Strategy name is required.")
    private String name;

    @NotEmpty(message = "Cannot be empty, Strategy description is required.")
    private String description;

    private String examples;

    private String hints;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public String getHints() {
        return hints;
    }

    public void setHints(String hints) {
        this.hints = hints;
    }
}

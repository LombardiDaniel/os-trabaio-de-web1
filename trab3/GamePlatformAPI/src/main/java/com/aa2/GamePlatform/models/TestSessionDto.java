package com.aa2.GamePlatform.models;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;

public class TestSessionDto {
    private Integer id;

    @NotNull(message = "Cannot be null, testerId required")
    private Integer testerId;
    
    @NotNull(message = "Cannot be null, projectId required")
    private Integer projectId;
    
    @NotNull(message = "Cannot be null, strategyId required")
    private Integer strategyId;

    private String status;
    private Instant startTime;
    private Instant endTime;

    public TestSessionDto() {}

    public TestSessionDto(Integer id, Integer testerId, Integer projectId, Integer strategyId, String status, Instant startTime, Instant endTime) {
        this.id = id;
        this.testerId = testerId;
        this.projectId = projectId;
        this.strategyId = strategyId;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTesterId() {
        return testerId;
    }

    public void setTesterId(Integer testerId) {
        this.testerId = testerId;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Integer strategyId) {
        this.strategyId = strategyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }
}

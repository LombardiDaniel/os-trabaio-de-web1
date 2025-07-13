package com.aa2.GamePlatform.models;

public class TestSessionDto {
    private Integer testerId;
    private Integer projectId;
    private Integer strategyId;
    private String status;

    public TestSessionDto() {}

    public TestSessionDto(Integer testerId, Integer projectId, Integer strategyId, String status) {
        this.testerId = testerId;
        this.projectId = projectId;
        this.strategyId = strategyId;
        this.status = status;
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
}

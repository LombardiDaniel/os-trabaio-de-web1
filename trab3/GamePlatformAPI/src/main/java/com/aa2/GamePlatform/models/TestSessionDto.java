package com.aa2.GamePlatform.models;

import java.util.List;

public class TestSessionDto {
    private Integer testerId;
    private Integer projectId;
    private Integer strategyId;
    private String status;
    private List<TestSession> testSessions;

    public TestSessionDto() {}

    public TestSessionDto(Integer testerId, Integer projectId, Integer strategyId, String status) {
        this.testerId = testerId;
        this.projectId = projectId;
        this.strategyId = strategyId;
        this.status = status;
    }

    public TestSessionDto(Integer testerId, Integer projectId, Integer strategyId, String status, List<TestSession> testSessions) {
        this.testerId = testerId;
        this.projectId = projectId;
        this.strategyId = strategyId;
        this.status = status;
        this.testSessions = testSessions;
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

    public List<TestSession> getTestSessions() {
        return testSessions;
    }

    public void setTestSessions(List<TestSession> testSessions) {
        this.testSessions = testSessions;
    }
}

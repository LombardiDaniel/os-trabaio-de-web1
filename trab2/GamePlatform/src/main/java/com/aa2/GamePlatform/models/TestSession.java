package com.aa2.GamePlatform.models;

import jakarta.persistence.*;

@Entity
@Table(name = "test_sessions")
public class TestSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tester_id", nullable = false)
    private Tester tester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "strategy_id", nullable = false)
    private Strategy strategy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TestSessionStatus status = TestSessionStatus.CREATED;

    @Column(nullable = false)
    private int durationMins;

    public TestSession() {}

    public TestSession(Tester tester, Project project, Strategy strategy) {
        this.tester = tester;
        this.project = project;
        this.strategy = strategy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tester getTester() {
        return tester;
    }

    public void setTester(Tester tester) {
        this.tester = tester;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public TestSessionStatus getStatus() {
        return status;
    }

    public void setStatus(TestSessionStatus status) {
        this.status = status;
    }

    public void incrementStatus() {
        switch (this.status) {
            case CREATED:
                this.setStatus(TestSessionStatus.IN_PROGRESS);
                break;
            case IN_PROGRESS:
                this.setStatus(TestSessionStatus.COMPLETED);
                break;
            case COMPLETED:
                break;
        }
    }
}

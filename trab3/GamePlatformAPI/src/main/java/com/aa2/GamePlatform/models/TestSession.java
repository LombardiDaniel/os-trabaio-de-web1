package com.aa2.GamePlatform.models;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

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

    @OneToMany(mappedBy="test_session", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bug> bugs;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TestSessionStatus status = TestSessionStatus.CREATED;

    @Column(name = "start_time", nullable = true)
    private Instant startTime = null;

    @Column(name = "end_time", nullable = true)
    private Instant endTime = null;

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
        if (this.status == TestSessionStatus.IN_PROGRESS) {
            Instant endTime = startTime.plusSeconds(durationMins * 60L);
            if (Instant.now().isAfter(endTime)) {
                this.setStatus(TestSessionStatus.COMPLETED);
                this.setEndTime(endTime);
                return this.status;
            }
        }
        return status;
    }

    public void setStatus(TestSessionStatus status) {
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

    public void incrementStatus() {
        switch (this.status) {
            case CREATED:
                this.setStatus(TestSessionStatus.IN_PROGRESS);
                this.setStartTime(Instant.now());
                break;
            case IN_PROGRESS:
                this.setStatus(TestSessionStatus.COMPLETED);
                this.setEndTime(Instant.now());
                break;
            case COMPLETED:
                break;
        }
    }

    public List<Bug> getBugs() {
        return bugs;
    }

    public void setBugs(List<Bug> bugs) {
        this.bugs = bugs;
    }
}

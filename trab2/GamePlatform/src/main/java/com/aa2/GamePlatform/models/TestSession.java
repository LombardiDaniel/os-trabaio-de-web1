package com.aa2.GamePlatform.models;

import jakarta.persistence.*;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "test_sessions")
public class TestSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "test_sessions_tester",
            joinColumns = @JoinColumn(name = "test_sessions_id"),
            inverseJoinColumns = @JoinColumn(name = "tester_id")
    )
    private Set<Tester> testers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "test_sessions_project",
            joinColumns = @JoinColumn(name = "test_sessions_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TestSessionStatus status = TestSessionStatus.CREATED;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Tester> getTesters() {
        return testers;
    }

    public void setTesters(Set<Tester> testers) {
        this.testers = testers;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public TestSessionStatus getStatus() {
        return status;
    }

    public void updateStatus() {
        switch (this.status) {
            case CREATED:
                this.status = TestSessionStatus.IN_PROGRESS;
                break;
            case IN_PROGRESS:
                this.status = TestSessionStatus.COMPLETED;
                break;
            case COMPLETED:
                break;
        }
    }
}

package com.aa2.GamePlatform.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(unique = true)
    private String projectName;

    private String projectDescription;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_tester",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "tester_id")
    )
    private Set<Tester> testers = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="project")
    private Set<TestSession> testSessions = new HashSet<>();

    public Project() {}

    public Project(String projectName, String projectDescription, Instant createdAt, Instant updatedAt, Set<Tester> testers) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.testers = testers;
    }

    public Project(String projectName, String projectDescription, Instant createdAt, Instant updatedAt) {
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Tester> getTesters() {
        return testers;
    }

    public void setTesters(Set<Tester> testers) {
        this.testers = testers;
    }

    public Set<TestSession> getTestSessions() {
        return testSessions;
    }

    public void setTestSessions(Set<TestSession> testSessions) {
        this.testSessions = testSessions;
    }
}

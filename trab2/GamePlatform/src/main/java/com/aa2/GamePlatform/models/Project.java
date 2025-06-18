package com.aa2.GamePlatform.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "projectname")
    private String projectName;

    @Column(name = "projectdescription")
    private String projectDescription;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @ManyToMany(mappedBy = "projects")
    private Set<TestSession> testSessions = new HashSet<>();

    public Project(Integer id, String projectName, String projectDescription, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Project() {};

    public Set<TestSession> getTestSessions() {
        return testSessions;
    }

    public void setTestSessions(Set<TestSession> testSessions) {
        this.testSessions = testSessions;
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
}

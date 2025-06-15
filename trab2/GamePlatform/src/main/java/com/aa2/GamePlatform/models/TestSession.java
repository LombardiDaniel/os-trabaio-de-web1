package com.aa2.GamePlatform.models;

import jakarta.persistence.*;

@Entity
@Table(name = "test_sessions")
public class TestSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "tester_id",
            referencedColumn = "id",
            nullable = false
    )
    private Tester tester;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "project_id",
            referencedColumn = "id",
            nullable = false
    )
    private Project project;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TestSessionStatus status = TestSessionStatus.CREATED;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Tester getTester() {
        return tester;
    }

    public void setTester(Tester tester) {
        this.tester = tester;
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

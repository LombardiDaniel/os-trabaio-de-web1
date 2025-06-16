package com.aa2.GamePlatform.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "test_sessions")
public class TestSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "tester_id",
//            referencedColumnName = "id",
//            nullable = false
//    )
//    private List<Tester> tester;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinColumn(
//            name = "project_id",
//            referencedColumnName = "id",
//            nullable = false
//    )
//    private List<Project> project;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TestSessionStatus status = TestSessionStatus.CREATED;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

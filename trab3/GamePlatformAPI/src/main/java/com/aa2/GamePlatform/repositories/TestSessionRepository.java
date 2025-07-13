package com.aa2.GamePlatform.repositories;

import com.aa2.GamePlatform.models.TestSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface TestSessionRepository extends JpaRepository<TestSession,Integer> {
    public ArrayList<TestSession> findByTesterId(Integer tester_id);
    public ArrayList<TestSession> findByProjectId(Integer project_id);
}

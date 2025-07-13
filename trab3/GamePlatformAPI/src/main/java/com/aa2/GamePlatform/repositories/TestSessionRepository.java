package com.aa2.GamePlatform.repositories;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aa2.GamePlatform.models.Project;
import com.aa2.GamePlatform.models.TestSession;
import com.aa2.GamePlatform.models.Tester;

public interface TestSessionRepository extends JpaRepository<TestSession,Integer> {
    public ArrayList<TestSession> findByTester(Tester tester);
    public ArrayList<TestSession> findByProject(Project project);
}

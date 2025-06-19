package com.aa2.GamePlatform.repositories;

import com.aa2.GamePlatform.models.TestSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestSessionRepository extends JpaRepository<TestSession,Integer> {
    //public TestSession findByTesterId(Integer id);
}

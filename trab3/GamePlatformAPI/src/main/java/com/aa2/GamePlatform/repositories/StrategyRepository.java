package com.aa2.GamePlatform.repositories;

import com.aa2.GamePlatform.models.Strategy;
import com.aa2.GamePlatform.models.TestSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface StrategyRepository extends JpaRepository<Strategy,Integer> {
}


package com.aa2.GamePlatform.repositories;

import com.aa2.GamePlatform.models.Bug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugRepository extends JpaRepository<Bug,Integer> {
}
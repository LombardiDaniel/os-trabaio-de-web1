package com.aa2.GamePlatform.repositories;

import com.aa2.GamePlatform.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
}

package com.aa2.GamePlatform.repositories;

import com.aa2.GamePlatform.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.lang.reflect.Array;
import java.util.ArrayList;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query("SELECT p FROM Project p JOIN p.testers t WHERE t.id = :testerId")
    List<Project> findByTesterId(@Param("testerId") Integer testerId);
}
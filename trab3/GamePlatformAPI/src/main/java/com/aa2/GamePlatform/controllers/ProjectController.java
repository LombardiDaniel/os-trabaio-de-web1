package com.aa2.GamePlatform.controllers;

import com.aa2.GamePlatform.models.*;
import com.aa2.GamePlatform.repositories.ProjectRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping({"project","projects"})
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping({"","/"})
    public Object getAllProjects(
        @RequestParam(value = "id", required = false) Integer id
    ) {
        if (id != null) {
            return projectRepository.findById(id).orElse(null);
        }
        return projectRepository.findAll();
    }

    public ResponseEntity<?> createProject(
            @Valid @RequestBody ProjectDto projectDto
    ) {

        Project project = new Project(
                projectDto.getProjectName(),
                projectDto.getProjectDescription(),
                new Date().toInstant(),
                new Date().toInstant()
        );

        try {
            projectRepository.save(project);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Project name is already used.");
        }

        return ResponseEntity.ok(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editProject(
            @PathVariable int id,
            @Valid @RequestBody ProjectDto projectDto
    ) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }

        project.setProjectName(projectDto.getProjectName());
        project.setProjectDescription(projectDto.getProjectDescription());
        project.setUpdatedAt(Instant.now());

        try {
            projectRepository.save(project);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Project name is already used.");
        }

        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable int id) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        projectRepository.delete(project);
        return ResponseEntity.ok().build();
    }
}

package com.aa2.GamePlatform.controllers;

import com.aa2.GamePlatform.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping({"/projects","project"})
    public String getProjects(Model model) {
        model.addAttribute("project", projectRepository.findAll());

        return "project/index";
    }
}

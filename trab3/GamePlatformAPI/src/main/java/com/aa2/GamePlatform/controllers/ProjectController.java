package com.aa2.GamePlatform.controllers;

import com.aa2.GamePlatform.models.Project;
import com.aa2.GamePlatform.models.ProjectDto;
import com.aa2.GamePlatform.models.Tester;
import com.aa2.GamePlatform.models.UserSession;
import com.aa2.GamePlatform.repositories.ProjectRepository;
import com.aa2.GamePlatform.repositories.UserSessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@RequestMapping({"projects"})
@Controller
public class ProjectController {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @GetMapping({"","/"})
    public String getProjects(Model model) {
        model.addAttribute("projects", projectRepository.findAll());
        return "project/index";
    }

    @GetMapping("/create")
    public String createProject(Model model, HttpServletRequest request) {
        Tester tester = getLoggedTester(request);
        if (!isAdmin(tester)) {
            return "redirect:/access-denied";
        }
        ProjectDto project = new ProjectDto();
        model.addAttribute("project", project);

        return "project/create";
    }

    @PostMapping("/create")
    public String createProject(
            @Valid @ModelAttribute ProjectDto project,
            BindingResult bindingResult,
            Model model,
            HttpServletRequest request
    ) {
        Tester tester = getLoggedTester(request);
        if (!isAdmin(tester)) {
            return "redirect:/access-denied";
        }

        model.addAttribute("project", project);

        if (bindingResult.hasErrors()) {
            return "project/create";
        }

        Project createdProject = new Project();
        createdProject.setProjectName(project.getProjectName());
        createdProject.setProjectDescription(project.getProjectDescription());
        createdProject.setCreatedAt(new Date().toInstant());
        createdProject.setUpdatedAt(new Date().toInstant());

        projectRepository.save(createdProject);

        return "redirect:/project";
    }

    @GetMapping("/edit")
    public String editProject(Model model, @RequestParam int id, HttpServletRequest request) {

        Tester tester = getLoggedTester(request);
        if (!isAdmin(tester)) {
            return "redirect:/access-denied";
        }

        Project projectToEdit = projectRepository.findById(id).orElse(null);

        if (projectToEdit == null) {
            return "redirect:/project";
        }

        ProjectDto updatedProject = new ProjectDto();
        updatedProject.setProjectName(projectToEdit.getProjectName());
        updatedProject.setProjectDescription(projectToEdit.getProjectDescription());

        model.addAttribute("project", projectToEdit);
        model.addAttribute("projectDto", updatedProject);

        return "project/edit";
    }

    @PostMapping("/edit")
    public String editProject(Model model,
                             @RequestParam int id,
                             @Valid @ModelAttribute ProjectDto projectDto,
                             BindingResult bindingResult, HttpServletRequest request) {

        Tester tester = getLoggedTester(request);
        if (!isAdmin(tester)) {
            return "redirect:/access-denied";
        }

        Project projectToEdit = projectRepository.findById(id).orElse(null);
        if (projectToEdit == null) {
            return "redirect:/project";
        }

        model.addAttribute("project", projectToEdit);

        if (bindingResult.hasErrors()) {
            return "project/edit";
        }

        projectToEdit.setProjectName(projectDto.getProjectName());
        projectToEdit.setProjectDescription(projectDto.getProjectDescription());
        projectToEdit.setUpdatedAt(new Date().toInstant());

        try {
            projectRepository.save(projectToEdit);
        }
        catch (Exception e) {
            return "project/edit";
        }

        return "redirect:/project";
    }

    @GetMapping("/delete")
    public String deleteProject(Model model, @RequestParam int id, HttpServletRequest request) {

        Tester tester = getLoggedTester(request);
        if (!isAdmin(tester)) {
            return "redirect:/access-denied";
        }

        Project projectToDelete = projectRepository.findById(id).orElse(null);

        if (projectToDelete != null) {
            projectRepository.delete(projectToDelete);
        }

        return "redirect:/project";
    }

    private Tester getLoggedTester(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSION_TOKEN".equals(cookie.getName())) {
                    UserSession session = userSessionRepository.findByToken(cookie.getValue());
                    if (session != null && session.getExpiresAt().isAfter(Instant.now())) {
                        return session.getTester();
                    }
                }
            }
        }
        return null;
    }

    private boolean isAdmin(Tester tester) {
        return tester != null && Boolean.TRUE.equals(tester.getUserAdmin());
    }
}

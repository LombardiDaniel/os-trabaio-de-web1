package com.aa2.GamePlatform.controllers;

import com.aa2.GamePlatform.models.StrategyDto;
import com.aa2.GamePlatform.models.TestSession;
import com.aa2.GamePlatform.repositories.StrategyRepository;
import com.aa2.GamePlatform.repositories.TestSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/test_sessions")
public class TestSessionController {
    private static final Logger log = LoggerFactory.getLogger(TestSessionController.class);

    @Autowired
    private StrategyRepository strategyRepository;

    @Autowired
    private TestSessionRepository testSessionRepository;

    @GetMapping({"", "/"})
    public String index(Model model)  {
        model.addAttribute("testSessions", testSessionRepository.findAll());

        return "test_sessions/index";
    }

    @PostMapping({"", "/"})
    public String createTestSession(
            Model model,
            TestSession testSession,
            @RequestParam int projectId
    ) {

        return "redirect:/test_sessions";
    }
}

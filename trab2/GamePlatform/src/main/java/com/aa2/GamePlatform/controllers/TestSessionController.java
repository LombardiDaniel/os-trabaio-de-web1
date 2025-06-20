package com.aa2.GamePlatform.controllers;

import com.aa2.GamePlatform.models.StrategyDto;
import com.aa2.GamePlatform.models.TestSession;
import com.aa2.GamePlatform.models.TestSessionDto;
import com.aa2.GamePlatform.repositories.ProjectRepository;
import com.aa2.GamePlatform.repositories.StrategyRepository;
import com.aa2.GamePlatform.repositories.TestSessionRepository;
import com.aa2.GamePlatform.repositories.TesterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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

    @Autowired
    private TesterRepository testerRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping({"", "/"})
    public String index(Model model)  {
        model.addAttribute("testSessions", testSessionRepository.findAll());

        return "test_sessions/index";
    }

    @PostMapping({"", "/"})
    public String createTestSession(
            Model model,
            TestSessionDto testSessionDto,
//            @RequestParam int projectId,
            BindingResult result
    ) {
        var testerOpt = testerRepository.findById(testSessionDto.getTesterId());
        var projectOpt = projectRepository.findById(testSessionDto.getProjectId());
        var strategyOpt = strategyRepository.findById(testSessionDto.getStrategyId());

        if (testerOpt.isEmpty() || projectOpt.isEmpty() || strategyOpt.isEmpty()) {
            model.addAttribute("error", "Invalid tester, project or strategy");
            return "redirect:/test_sessions";
        }

        TestSession testSession = new TestSession(
                testerOpt.get(),
                projectOpt.get(),
                strategyOpt.get()
        );

        try {
            testSessionRepository.save(testSession);
        }  catch (Exception ex) {
//            result.addError(
//                    new FieldError("strategyDto", "name", strategyDto.getName(), false, null, null, "Strategy name is already used")
//            );
        }

        return "redirect:/test_sessions";
    }
}

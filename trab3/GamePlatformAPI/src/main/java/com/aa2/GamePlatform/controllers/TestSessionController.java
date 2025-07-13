package com.aa2.GamePlatform.controllers;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aa2.GamePlatform.models.TestSession;
import com.aa2.GamePlatform.models.TestSessionDto;
import com.aa2.GamePlatform.repositories.ProjectRepository;
import com.aa2.GamePlatform.repositories.StrategyRepository;
import com.aa2.GamePlatform.repositories.TestSessionRepository;
import com.aa2.GamePlatform.repositories.TesterRepository;
import com.aa2.GamePlatform.repositories.UserSessionRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
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

    @Autowired
    private UserSessionRepository userSessionRepository;

    @GetMapping({"", "/"})
    public Object getTestSessions(
        @RequestParam(value = "id", required = false) Integer id,
        @RequestParam(value = "tester_id", required = false) Integer testerId,
        @RequestParam(value = "project_id", required = false) Integer projectId
    ) {
        if (id != null) {
            return testSessionRepository.findById(id)
            .map(ts -> new TestSessionDto(
                ts.getId(),
                ts.getTester().getId(),
                ts.getProject().getId(),
                ts.getStrategy().getId(),
                ts.getStatus().toString(),
                ts.getStartTime(),
                ts.getEndTime()
            ))
            .orElse(null);
        }
        if (testerId != null) {
            var testerOpt = testerRepository.findById(testerId);
            if (testerOpt.isPresent()) {
                return testSessionRepository.findByTester(testerOpt.get())
                    .stream()
                    .map(ts -> new TestSessionDto(
                        ts.getId(),
                        ts.getTester().getId(),
                        ts.getProject().getId(),
                        ts.getStrategy().getId(),
                        ts.getStatus().toString(),
                        ts.getStartTime(),
                        ts.getEndTime()
                    ))
                    .toList();
            } else {
                return new ArrayList<>();
            }
        }
        if (projectId != null) {
            var projectOpt = projectRepository.findById(projectId);
            if (projectOpt.isPresent()) {
                return testSessionRepository.findByProject(projectOpt.get())
                    .stream()
                    .map(ts -> new TestSessionDto(
                        ts.getId(),
                        ts.getTester().getId(),
                        ts.getProject().getId(),
                        ts.getStrategy().getId(),
                        ts.getStatus().toString(),
                        ts.getStartTime(),
                        ts.getEndTime()
                    ))
                    .toList();
            } else {
                return new ArrayList<>();
            }
        }
        return testSessionRepository.findAll()
            .stream()
            .map(ts -> new TestSessionDto(
                ts.getId(),
                ts.getTester().getId(),
                ts.getProject().getId(),
                ts.getStrategy().getId(),
                ts.getStatus().toString(),
                ts.getStartTime(),
                ts.getEndTime()
            ))
            .toList();
    }

    @PostMapping({"", "/"})
    public ResponseEntity<?> createTestSession(
        @Valid @RequestBody TestSessionDto testSessionDto,
        HttpServletRequest request
    ) {
        var projectOpt = projectRepository.findById(testSessionDto.getProjectId());
        var strategyOpt = strategyRepository.findById(testSessionDto.getStrategyId());
        var testerOpt = testerRepository.findById(testSessionDto.getTesterId());

        if (projectOpt.isEmpty() || strategyOpt.isEmpty() || testerOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid project, strategy or tester");
        }

        TestSession testSession = new TestSession(
            testerOpt.get(),
            projectOpt.get(),
            strategyOpt.get()
        );

        testSessionRepository.save(testSession);

        return ResponseEntity.status(HttpStatus.CREATED).body(testSession);
    }

    @PostMapping("/status/{id}")
    public ResponseEntity<?> incrementTestSession(@PathVariable int id) {
        TestSession testSession = testSessionRepository.findById(id).orElse(null);
        if (testSession == null) {
            return ResponseEntity.notFound().build();
        }
        testSession.incrementStatus();
        testSessionRepository.save(testSession);
        return ResponseEntity.ok().build();
    }

//    @GetMapping({"", "/"})
//    public String index(
//            Model model,
//            HttpServletRequest request,
//            @RequestParam(name = "projectId", required = false) Integer projectId
//    )  {
//        Tester tester = getLoggedTester(request);
//        if (tester == null) {
//            return "redirect:/access-denied";
//        }
//
//        if (projectId != null) {
//            model.addAttribute("testSessions", testSessionRepository.findByProjectId(projectId));
//        } else {
//            if (isAdmin(tester)) {
//                model.addAttribute("testSessions", testSessionRepository.findAll());
//            } else {
//                model.addAttribute("testSessions", testSessionRepository.findByTesterId(tester.getId()));
//            }
//        }
//
//        if (isAdmin(tester)) {
//            model.addAttribute("projects", projectRepository.findAll());
//        } else {
//            model.addAttribute("projects", projectRepository.findByTesterId(tester.getId()));
//        }
//
//        return "test_sessions/index";
//    }
//
//    @GetMapping("/create")
//    public String create(Model model, HttpServletRequest request) {
//        Tester tester = getLoggedTester(request);
//        if (tester == null) {
//            return "redirect:/access-denied";
//        }
//
//        model.addAttribute("strategies", strategyRepository.findAll());
//        model.addAttribute("testSessionDto", new TestSessionDto());
//
//        if (isAdmin(tester)) {
//            model.addAttribute("projects", projectRepository.findAll());
//        } else {
//            model.addAttribute("projects", projectRepository.findByTesterId(tester.getId()));
//        }
//
//        return "test_sessions/create";
//    }
//
//    @PostMapping({"", "/"})
//    public String createTestSession(
//            Model model,
//            TestSessionDto testSessionDto,
////            @RequestParam int projectId,
//            BindingResult result,
//            HttpServletRequest request
//    ) {
//        Tester tester = getLoggedTester(request);
//        if (tester == null) {
//            return "redirect:/access-denied";
//        }
//
//        var projectOpt = projectRepository.findById(testSessionDto.getProjectId());
//        var strategyOpt = strategyRepository.findById(testSessionDto.getStrategyId());
//
//        if (projectOpt.isEmpty() || strategyOpt.isEmpty()) {
//            model.addAttribute("error", "Invalid tester, project or strategy");
//            return "redirect:/test_sessions";
//        }
//
//        log.warn("{} {} {}", tester.getId(), projectOpt.get().getId(), strategyOpt.get().getId());
//
//        TestSession testSession = new TestSession(
//                tester,
//                projectOpt.get(),
//                strategyOpt.get()
//        );
//
//        testSessionRepository.save(testSession);
//
//        return "redirect:/test_sessions";
//    }
//
//    @PostMapping("/increment/{id}")
//    public String incrementSession(
//            HttpServletRequest request,
//            @PathVariable("id") int sessionId
//        ) {
//        log.warn("Incrementing test session id {}", sessionId);
//        Tester tester = getLoggedTester(request);
//        if (tester == null) {
//            return "redirect:/access-denied";
//        }
//
//        var tgtSessionOpt = testSessionRepository.findById(sessionId);
//        if (tgtSessionOpt.isEmpty()) {
//            return "redirect:/access-denied";
//        }
//
//        var tgtSession = tgtSessionOpt.get();
//        if (!tester.getId().equals(tgtSession.getTester().getId())) {
//            return "redirect:/access-denied";
//        }
//
//        tgtSession.incrementStatus();
//        testSessionRepository.save(tgtSession);
//        return "redirect:/test_sessions";
//    }
//
//
//    private Tester getLoggedTester(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("SESSION_TOKEN".equals(cookie.getName())) {
//                    UserSession session = userSessionRepository.findByToken(cookie.getValue());
//                    if (session != null && session.getExpiresAt().isAfter(Instant.now())) {
//                        return session.getTester();
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    private boolean isAdmin(Tester tester) {
//        return tester != null && Boolean.TRUE.equals(tester.getUserAdmin());
//    }
}

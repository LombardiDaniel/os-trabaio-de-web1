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
import org.springframework.web.bind.annotation.PutMapping;
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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping({"test_session","test_sessions"})
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

        TestSessionDto dto = new TestSessionDto(
            testSession.getId(),
            testSession.getTester().getId(),
            testSession.getProject().getId(),
            testSession.getStrategy().getId(),
            testSession.getStatus().toString(),
            testSession.getStartTime(),
            testSession.getEndTime()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> incrementTestSession(@PathVariable int id) {
        TestSession testSession = testSessionRepository.findById(id).orElse(null);
        if (testSession == null) {
            return ResponseEntity.notFound().build();
        }
        testSession.incrementStatus();
        testSessionRepository.save(testSession);
        return ResponseEntity.ok().build();
    }
}

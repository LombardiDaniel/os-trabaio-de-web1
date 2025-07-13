package com.aa2.GamePlatform.controllers;

import com.aa2.GamePlatform.models.*;
import com.aa2.GamePlatform.repositories.BugRepository;
import com.aa2.GamePlatform.repositories.TesterRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping({"bug", "bugs"})
public class BugController {
    private static final Logger log = LoggerFactory.getLogger(BugController.class);

    @Autowired
    private BugRepository bugRepository;

    @GetMapping({"","/"})
    public Object getAllBugs(
            @RequestParam(value = "id", required = false) Integer id
    ) {
        if (id != null) {
            return bugRepository.findById(id).orElse(null);
        }
        return bugRepository.findAll();
    }

    @PostMapping({"","/"})
    public ResponseEntity<?> createTester(
            @Valid @RequestBody BugDto bugDto
    ) {

        Bug bug = new Bug(
                bugDto.getTitle(),
                bugDto.getDescription(),
                bugDto.getStepsToReproduce(),
                bugDto.getStatus(),
                bugDto.getPriority(),
                bugDto.getSeverity(),
                new Date().toInstant(),
                new Date().toInstant()
        );

        try {
            bugRepository.save(bug);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bug title is already used.");
        }

        return ResponseEntity.ok(bug);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editTester(
            @PathVariable int id,
            @Valid @RequestBody BugDto bugDto
    ) {
        Bug bug = bugRepository.findById(id).orElse(null);
        if (bug == null) {
            return ResponseEntity.notFound().build();
        }

        bug.setTitle(bugDto.getTitle());
        bug.setDescription(bugDto.getDescription());
        bug.setStepsToReproduce(bugDto.getStepsToReproduce());
        bug.setStatus(bugDto.getStatus());
        bug.setPriority(bugDto.getPriority());
        bug.setSeverity(bugDto.getSeverity());
        bug.setUpdatedAt(Instant.now());

        try {
            bugRepository.save(bug);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Bug title is already used.");
        }

        return ResponseEntity.ok(bug);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTester(@PathVariable int id) {
        Bug bug = bugRepository.findById(id).orElse(null);
        if (bug == null) {
            return ResponseEntity.notFound().build();
        }
        bugRepository.delete(bug);
        return ResponseEntity.ok().build();
    }
}

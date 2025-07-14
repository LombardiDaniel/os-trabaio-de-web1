package com.aa2.GamePlatform.controllers;

import com.aa2.GamePlatform.models.Tester;
import com.aa2.GamePlatform.models.TesterDto;
import com.aa2.GamePlatform.repositories.TesterRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping({"tester", "testers"})
public class TesterController {
    private static final Logger log = LoggerFactory.getLogger(TesterController.class);

    @Autowired
    private TesterRepository testerRepository;

    @GetMapping({"","/"})
    public Object getAllTesters(
        @RequestParam(value = "id", required = false) Integer id
    ) {
        if (id != null) {
            return testerRepository.findById(id).orElse(null);
        }
        return testerRepository.findAll();
    }

    @PostMapping({"","/"})
    public ResponseEntity<?> createTester(
            @Valid @RequestBody TesterDto testerDto
    ) {

        Tester tester = new Tester(
                testerDto.getFirstName(),
                testerDto.getLastName(),
                testerDto.getEmail(),
                new BCryptPasswordEncoder().encode(testerDto.getPassword()),
                new Date().toInstant(),
                new Date().toInstant(),
                testerDto.getIsUserAdmin()
        );

        try {
            testerRepository.save(tester);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Tester email is already used.");
        }

        return ResponseEntity.ok(tester);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editTester(
            @PathVariable int id,
            @Valid @RequestBody TesterDto testerDto
    ) {
        Tester tester = testerRepository.findById(id).orElse(null);
        if (tester == null) {
            return ResponseEntity.notFound().build();
        }

        tester.setFirstName(testerDto.getFirstName());
        tester.setLastName(testerDto.getLastName());
        tester.setEmail(testerDto.getEmail());
        tester.setUpdatedAt(Instant.now());

        try {
            testerRepository.save(tester);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Tester email is already used.");
        }

        return ResponseEntity.ok(tester);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTester(@PathVariable int id) {
        Tester tester = testerRepository.findById(id).orElse(null);
        if (tester == null) {
            return ResponseEntity.notFound().build();
        }
        testerRepository.delete(tester);
        return ResponseEntity.ok().build();
    }
}

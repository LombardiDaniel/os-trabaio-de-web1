package com.aa2.GamePlatform.controllers;

import com.aa2.GamePlatform.models.Tester;
import com.aa2.GamePlatform.models.TesterDto;
import com.aa2.GamePlatform.repositories.TesterRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping({"tester"})
public class TesterController {
    private static final Logger log = LoggerFactory.getLogger(TesterController.class);

    @Autowired
    private TesterRepository testerRepository;

    @Operation(summary = "List all testers", description = "List all testers in database.")
    @GetMapping("/{id}")
    public Tester findById(@PathVariable final Integer id) {
        return testerRepository.findById(id).orElse(null);
    }

    @Operation(summary = "List all testers", description = "List all testers in database.")
    @GetMapping("")
    public List<Tester> tester() {
        return testerRepository.findAll();
    }

    @Operation(summary = "Create user that is a tester", description = "Create a simple tester into database")
    @PostMapping("/create")
    public Tester createTester(@RequestBody Tester tester) {

        if (testerRepository.findByEmail(tester.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email '" + tester.getEmail() + "' is already in use.");
        }

        String hashedPassword = new BCryptPasswordEncoder().encode(tester.getPassword());

        Tester createdTester = new Tester();
        createdTester.setFirstName(tester.getFirstName());
        createdTester.setLastName(tester.getLastName());
        createdTester.setEmail(tester.getEmail());
        createdTester.setPassword(hashedPassword);
        createdTester.setUserAdmin(tester.getUserAdmin());
        createdTester.setCreatedAt(new Date().toInstant());
        createdTester.setUpdatedAt(new Date().toInstant());

        return testerRepository.save(createdTester);
    }

    @PostMapping("/edit")
    public Tester editTester(@PathVariable final Integer id, @RequestBody Tester tester) {

        Tester testerToEdit = testerRepository.findById(id).orElse(null);

        if (testerToEdit == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User id: '" + id + "' does not exist.");
        }

        testerToEdit.setFirstName(tester.getFirstName());
        testerToEdit.setLastName(tester.getLastName());
        testerToEdit.setEmail(tester.getEmail());
        testerToEdit.setUserAdmin(tester.getUserAdmin());
        testerToEdit.setUpdatedAt(new Date().toInstant());

        return testerRepository.save(testerToEdit);
    }

    @Operation(summary = "Delete user that is a tester", description = "Delete a simple tester into database")
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteTester(@PathVariable final Integer id) {

        Tester testerToDelete = testerRepository.findById(id).orElse(null);

        if (testerToDelete != null) {
            testerRepository.delete(testerToDelete);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}

package com.aa2.GamePlatform.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aa2.GamePlatform.models.Strategy;
import com.aa2.GamePlatform.models.StrategyDto;
import com.aa2.GamePlatform.repositories.StrategyRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/strategies")
public class StrategyController {
    private static final Logger log = LoggerFactory.getLogger(StrategyController.class);

    @Autowired
    private StrategyRepository strategyRepository;

    @GetMapping({"", "/"})
    public Object getAllStrategies(
        @RequestParam(value = "id", required = false) Integer id
    ) {
        if (id != null) {
            return strategyRepository.findById(id).orElse(null);
        }
        return strategyRepository.findAll();
    }

    @PostMapping({"", "/"})
    public ResponseEntity<?> createStrategy(
        @Valid @RequestBody StrategyDto strategyDto
    ) {
        Strategy strategy = new Strategy(
            strategyDto.getName(),
            strategyDto.getDescription(),
            strategyDto.getExamples(),
            strategyDto.getHints()
        );

        try {
            strategyRepository.save(strategy);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Strategy name is already used.");
        }

        return ResponseEntity.ok(strategy);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStrategy(
        @PathVariable int id,
        @Valid @RequestBody StrategyDto strategyDto
    ) {
        Strategy strategy = strategyRepository.findById(id).orElse(null);
        if (strategy == null) {
            return ResponseEntity.notFound().build();
        }

        strategy.setName(strategyDto.getName());
        strategy.setDescription(strategyDto.getDescription());
        strategy.setExamples(strategyDto.getExamples());
        strategy.setHints(strategyDto.getHints());

        try {
            strategyRepository.save(strategy);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Strategy name is already used.");
        }

        return ResponseEntity.ok(strategy);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStrategy(@PathVariable int id) {
        Strategy strategy = strategyRepository.findById(id).orElse(null);
        if (strategy == null) {
            return ResponseEntity.notFound().build();
        }
        strategyRepository.delete(strategy);
        return ResponseEntity.ok().build();
    }
}

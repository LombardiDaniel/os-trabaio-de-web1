package com.aa2.GamePlatform.controllers;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aa2.GamePlatform.models.Strategy;
import com.aa2.GamePlatform.models.StrategyDto;
import com.aa2.GamePlatform.models.Tester;
import com.aa2.GamePlatform.models.UserSession;
import com.aa2.GamePlatform.repositories.StrategyRepository;
import com.aa2.GamePlatform.repositories.UserSessionRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/strategies")
public class StrategyController {
    private static final Logger log = LoggerFactory.getLogger(StrategyController.class);

    @Autowired
    private StrategyRepository strategyRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

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
    public String createStrategy(
            @ModelAttribute StrategyDto strategyDto,
            Model model,
            BindingResult result,
            HttpServletRequest request
    )  {
        Tester tester = getLoggedTester(request);
        if (!isAdmin(tester)) {
            return "redirect:/access-denied";
        }

        if (strategyDto.getDescription() != null && strategyDto.getDescription().isEmpty()) {
            result.addError(
                    new FieldError("strategyDto", "description", "Strategy description is required.")
            );
        }

        if (strategyDto.getName() != null && strategyDto.getName().isEmpty()) {
            result.addError(
                    new FieldError("strategyDto", "name", "Strategy name is required.")
            );
        }

        if (result.hasErrors()) {
            model.addAttribute("strategyDto", strategyDto);
            model.addAttribute("strategies", strategyRepository.findAll());
            return "strategies/index";
        }

        Strategy strategy =  new Strategy(strategyDto.getName(), strategyDto.getDescription(), strategyDto.getExamples(), strategyDto.getHints());

        try {
            strategyRepository.save(strategy);
        } catch (Exception e) {
            result.addError(
                    new FieldError("strategyDto", "name", strategyDto.getName(), false, null, null, "Strategy name is already used")
            );

            model.addAttribute("strategyDto", strategyDto);
            model.addAttribute("strategies", strategyRepository.findAll());

            return "strategies/index";
        }

        return "redirect:/strategies";
    }

    @GetMapping("/delete")
    public String deleteStrategy(@RequestParam int id, HttpServletRequest request) {
        Tester tester = getLoggedTester(request);
        if (!isAdmin(tester)) {
            return "redirect:/access-denied";
        }
        Strategy strategy = strategyRepository.findById(id).orElse(null);
        if (strategy != null) {
            strategyRepository.delete(strategy);
        }

        return "redirect:/strategies";
    }

    @PostMapping("edit")
    public String editStrategy(
            Model model,
            @RequestParam int id,
            @ModelAttribute StrategyDto strategyDto,
            BindingResult result,
            HttpServletRequest request
    ) {
        Tester tester = getLoggedTester(request);
        if (!isAdmin(tester)) {
            return "redirect:/access-denied";
        }
        Strategy strategy = strategyRepository.findById(id).orElse(null);
        if (strategy == null) {
            return "redirect:/strategies";
        }

        model.addAttribute("strategy", strategy);

        if (result.hasErrors()) {
            return "strategies/edit";
        }

        strategy.setName(strategyDto.getName());
        strategy.setDescription(strategyDto.getDescription());
        strategy.setExamples(strategyDto.getExamples());
        strategy.setHints(strategyDto.getHints());

        try {
            strategyRepository.save(strategy);
        } catch (Exception e) {
            result.addError(
                    new FieldError("strategyDto", "name", strategyDto.getName(), false, null, null, "Strategy Name is already used")
            );

            return "strategies/edit";
        }

        return "redirect:/strategies";
    }

    @GetMapping("edit")
    public String editClients(Model model, @RequestParam int id, HttpServletRequest request) {
        Tester tester = getLoggedTester(request);
        if (!isAdmin(tester)) {
            return "redirect:/access-denied";
        }
        Strategy strategy = strategyRepository.findById(id).orElse(null);
        if (strategy == null) {
            return "redirect:/strategies";
        }

        StrategyDto strategyDto = new StrategyDto();
        strategyDto.setName(strategy.getName());
        strategyDto.setDescription(strategy.getDescription());
        strategyDto.setExamples(strategy.getExamples());
        strategyDto.setHints(strategy.getHints());

        model.addAttribute("strategy", strategy);
        model.addAttribute("strategyDto", strategyDto);

        return "strategies/edit";
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

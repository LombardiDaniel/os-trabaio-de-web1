package com.aa2.GamePlatform.controllers;

import jakarta.validation.Valid;
import com.aa2.GamePlatform.models.Strategy;
import com.aa2.GamePlatform.models.StrategyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import com.aa2.GamePlatform.repositories.StrategyRepository;
import org.springframework.validation.FieldError;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Controller
@RequestMapping("/strategies")
public class StrategyController {
    private static final Logger log = LoggerFactory.getLogger(StrategyController.class);

    @Autowired
    private StrategyRepository strategyRepository;

    @GetMapping({"", "/"})
    public String index(Model model)  {

        StrategyDto strategyDto = new StrategyDto();

        model.addAttribute("strategyDto", strategyDto);
        model.addAttribute("strategies", strategyRepository.findAll());

        return "strategies/index";
    }

    @PostMapping({"", "/"})
    public String createStrategy(
            @ModelAttribute StrategyDto strategyDto,
            Model model,
            BindingResult result
    )  {

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
    public String deleteStrategy(@RequestParam int id) {
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
            BindingResult result
    ) {

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
    public String editClients(Model model, @RequestParam int id) {
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
}

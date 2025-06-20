package com.aa2.GamePlatform.controllers;

import com.aa2.GamePlatform.repositories.TesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TesterController {
    @Autowired
    private TesterRepository testerRepository;

    @GetMapping({"testers", "tester"})
    public String tester(Model model) {
        model.addAttribute("testers", testerRepository.findAll());

        return "tester/index";
    }
}

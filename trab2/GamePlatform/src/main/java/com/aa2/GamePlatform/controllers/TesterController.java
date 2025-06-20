package com.aa2.GamePlatform.controllers;

import com.aa2.GamePlatform.models.Tester;
import com.aa2.GamePlatform.models.TesterDto;
import com.aa2.GamePlatform.repositories.TesterRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping({"tester", "testers"})
public class TesterController {
    @Autowired
    private TesterRepository testerRepository;

    @GetMapping({"","/"})
    public String tester(Model model) {
        model.addAttribute("testers", testerRepository.findAll());

        return "tester/index";
    }

    @GetMapping("/create")
    public String createTester(Model model) {
        TesterDto tester = new TesterDto();
        model.addAttribute("tester", tester);

        return "tester/create";
    }

    @PostMapping("/create")
    public String createTester(@Valid @ModelAttribute TesterDto tester,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "tester/create";
        }

        Tester createdTester = new Tester();
        createdTester.setFirstName(tester.getFirstName());
        createdTester.setLastName(tester.getLastName());
        createdTester.setEmail(tester.getEmail());
        createdTester.setUserAdmin(tester.getUserAdmin());
        createdTester.setCreatedAt(new Date().toInstant());
        createdTester.setUpdatedAt(new Date().toInstant());

        testerRepository.save(createdTester);

        return "redirect:/tester";
    }

    @GetMapping("/edit")
    public String editTester(Model model, @RequestParam int id) {

        Tester testerToEdit = testerRepository.findById(id).orElse(null);

        if (testerToEdit == null) {
            return "redirect:/tester";
        }

        TesterDto updatedTester = new TesterDto();
        updatedTester.setFirstName(testerToEdit.getFirstName());
        updatedTester.setLastName(testerToEdit.getLastName());
        updatedTester.setEmail(testerToEdit.getEmail());
        updatedTester.setUserAdmin(testerToEdit.getUserAdmin());

        model.addAttribute("tester", testerToEdit);
        model.addAttribute("testerDto", updatedTester);

        return "tester/edit";
    }

    @PostMapping("/edit")
    public String editTester(Model model,
                             @RequestParam int id,
                             @Valid @ModelAttribute TesterDto testerDto,
                             BindingResult bindingResult) {

        Tester testerToEdit = testerRepository.findById(id).orElse(null);
        if (testerToEdit == null) {
            return "redirect:/tester";
        }

        model.addAttribute("tester", testerToEdit);

        if (bindingResult.hasErrors()) {
            return "tester/edit";
        }

        testerToEdit.setFirstName(testerDto.getFirstName());
        testerToEdit.setLastName(testerDto.getLastName());
        testerToEdit.setEmail(testerDto.getEmail());
        testerToEdit.setUserAdmin(testerDto.getUserAdmin());
        testerToEdit.setUpdatedAt(new Date().toInstant());

        try {
            testerRepository.save(testerToEdit);
        }
        catch (Exception e) {
            bindingResult.addError(new FieldError(
                    "testerDto",
                    "email",
                    testerDto.getEmail(),
                    false, null, null,
                    "Email Already in use")
            );
            return "tester/edit";
        }

        return "redirect:/tester";
    }

    @GetMapping("/delete")
    public String deleteTester(Model model, @RequestParam int id) {

        Tester testerToDelete = testerRepository.findById(id).orElse(null);

        if (testerToDelete != null) {
            testerRepository.delete(testerToDelete);
        }

        return "redirect:/tester";
    }
}

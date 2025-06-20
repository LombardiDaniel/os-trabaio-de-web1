package com.aa2.GamePlatform.controllers;

import com.aa2.GamePlatform.dto.AdminDTO;
import com.aa2.GamePlatform.models.Admin;
import com.aa2.GamePlatform.repositories.AdminRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/admins")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    // --- Listar todos os administradores (Read) ---
    @GetMapping
    public String listarAdmins(Model model) {
        List<Admin> admins = adminRepository.findAll();
        model.addAttribute("admins", admins);
        return "admin/list";
    }

    // --- Exibir o formulário de criação de novo administrador (Create - parte 1) ---
    @GetMapping("/novo")
    public String exibirFormularioCriacao(Model model) {
        model.addAttribute("adminDTO", new AdminDTO());
        return "admin/form";
    }

    // --- Salvar um novo administrador ou atualizar um existente (Create/Update - parte 2) ---
    @PostMapping("/salvar")
    public String salvarAdmin(@Valid @ModelAttribute("adminDTO") AdminDTO adminDTO,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {

        // Validação de E-mail Único:
        if (adminDTO.getId() == null && adminRepository.existsByEmail(adminDTO.getEmail())) {
            result.rejectValue("email", "error.adminDTO",
                    messageSource.getMessage("validation.email.unique", null, LocaleContextHolder.getLocale()));
        }
        if (adminDTO.getId() != null && adminRepository.existsByEmailAndIdNot(adminDTO.getEmail(), adminDTO.getId())) {
            result.rejectValue("email", "error.adminDTO",
                    messageSource.getMessage("validation.email.unique_other", null, LocaleContextHolder.getLocale()));
        }

        if (result.hasErrors()) {
            return "admin/form";
        }

        Admin admin = new Admin();
        if (adminDTO.getId() != null) { // Se o ID existe, é uma EDIÇÃO
            Optional<Admin> existingAdmin = adminRepository.findById(adminDTO.getId());
            if (existingAdmin.isPresent()) {
                admin = existingAdmin.get();
                // A senha só é atualizada se o campo no DTO NÃO estiver vazio.
                if (!adminDTO.getSenha().isEmpty()) {
                    admin.setSenha(passwordEncoder.encode(adminDTO.getSenha()));
                }
            } else {
                redirectAttributes.addFlashAttribute("errorMessage",
                        messageSource.getMessage("message.admin.not_found", null, LocaleContextHolder.getLocale()));
                return "redirect:/admin/admins";
            }
        } else { // Se o ID é nulo, é uma CRIAÇÃO
            admin.setSenha(passwordEncoder.encode(adminDTO.getSenha()));
        }

        BeanUtils.copyProperties(adminDTO, admin, "id", "senha");

        adminRepository.save(admin);
        redirectAttributes.addFlashAttribute("successMessage",
                messageSource.getMessage("message.admin.saved.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/admin/admins";
    }

    // --- Exibir o formulário de edição de administrador (Update - parte 1) ---
    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Admin> adminOptional = adminRepository.findById(id);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            AdminDTO adminDTO = new AdminDTO();
            BeanUtils.copyProperties(admin, adminDTO);
            adminDTO.setSenha("");
            model.addAttribute("adminDTO", adminDTO);
            return "admin/form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("message.admin.not_found", null, LocaleContextHolder.getLocale()));
            return "redirect:/admin/admins";
        }
    }

    // --- Deletar um administrador (Delete) ---
    @GetMapping("/deletar/{id}")
    public String deletarAdmin(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (adminRepository.existsById(id)) {
            try {
                adminRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("successMessage",
                        messageSource.getMessage("message.admin.deleted.success", null, LocaleContextHolder.getLocale()));
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("errorMessage",
                        messageSource.getMessage("message.admin.delete.error", null, LocaleContextHolder.getLocale()));
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    messageSource.getMessage("message.admin.not_found", null, LocaleContextHolder.getLocale()));
        }
        return "redirect:/admin/admins";
    }
}
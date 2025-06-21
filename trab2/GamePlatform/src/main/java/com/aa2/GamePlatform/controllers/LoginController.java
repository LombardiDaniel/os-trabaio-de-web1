package com.aa2.GamePlatform.controllers;

import com.aa2.GamePlatform.models.Tester;
import com.aa2.GamePlatform.models.UserSession;
import com.aa2.GamePlatform.repositories.TesterRepository;
import com.aa2.GamePlatform.repositories.UserSessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private TesterRepository testerRepository;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @GetMapping
    public String loginPage() {
        return "login/index";
    }

    @PostMapping
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpServletResponse response,
                        Model model) {
        // Find tester by email
        Tester tester = testerRepository.findByEmail(email);

        // Check password (assuming plain text for demo; use hashing in production)
        if (tester == null || !password.equals(tester.getPassword())) {
            model.addAttribute("error", true);
            return "login/index";
        }

        // Create session
        Instant now = Instant.now();
        Instant expiresAt = now.plus(1, ChronoUnit.DAYS);
        UserSession session = new UserSession(tester, now, expiresAt);
        userSessionRepository.save(session);

        // Set session token as cookie
        Cookie cookie = new Cookie("SESSION_TOKEN", session.getToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 1 day
        response.addCookie(cookie);

        return "redirect:/";
    }
}
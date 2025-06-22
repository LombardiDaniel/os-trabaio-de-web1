package com.aa2.GamePlatform.controllers;

import com.aa2.GamePlatform.models.StrategyDto;
import com.aa2.GamePlatform.models.Tester;
import com.aa2.GamePlatform.models.UserSession;
import com.aa2.GamePlatform.repositories.UserSessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Instant;

@Controller
public class HomeController {

    @Autowired
    private UserSessionRepository userSessionRepository;

    @GetMapping("/")
    public String home(
            Model model,
            HttpServletRequest request
    )  {
        Tester tester = getLoggedTester(request);

        model.addAttribute("user", tester);

        return "index";
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
}

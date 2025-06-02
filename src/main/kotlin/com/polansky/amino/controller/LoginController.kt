package com.polansky.amino.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes


@Controller
@RequestMapping("/api")
class LoginController {


    @GetMapping("/login")
    fun showLoginForm(): String {
        return "login"
    }

    @PostMapping("/login")
    fun processLogin(
        @RequestParam("email") email: String,
        @RequestParam("password") password: String,
        model: Model,
        redirectAttrs: RedirectAttributes
    ): String {
        val DEMO_EMAIL = "user@example.com"
        val DEMO_PASSWORD = "password123"

        // (A) Basic form validation
        if (email.isBlank() || password.isBlank()) {
            model.addAttribute("errorMessage", "Email and password are required.")
            return "login" // re‐render login.html with error
        }

        // (B) Check credentials (replace with your real authentication logic)
        if (email != DEMO_EMAIL || password != DEMO_PASSWORD) {
            // Credentials are invalid ⇒ re‐render login with an error message
            model.addAttribute("errorMessage", "Invalid email or password.")
            return "login"
        }

        // (C) If valid ⇒ redirect to home. You could set a session attribute or a cookie here.
        // For demo, we just redirect:
        redirectAttrs.addFlashAttribute("welcomeMessage", "Welcome back, $email!")
        return "redirect:/api/home"
    }

}
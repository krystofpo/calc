package com.polansky.amino.controller

import com.polansky.amino.service.LoginResult
import com.polansky.amino.service.LoginResult.*
import com.polansky.amino.service.LoginService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes


@Controller
@RequestMapping("/api/v1/auth")
class LoginController(val loginService: LoginService) {


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
        val result = loginService.tryLogin(email, password)
        return when (result) {
            OK -> {
                redirectAttrs.addFlashAttribute("welcomeMessage", "Success")
                "redirect:/api/v1/home"
            }

            BLANK_EMAIL, BLANK_PASSWORD -> {
                model.addAttribute("errorMessage", "Non empty email and password are required.")
                "login"
            }

            UNKNOWN_EMAIL -> {
                model.addAttribute("errorMessage", "Unknown email.")
                "login"
            }

            WRONG_PASSWORD -> {
                model.addAttribute("errorMessage", "Wrong password.")
                "login"
            }
        }
    }

}
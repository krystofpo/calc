package com.polansky.amino.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/api")
class HomeController {

    @GetMapping("/home")
    fun home(model: Model?): String {
        // If you used RedirectAttributes.addFlashAttribute("welcomeMessage", …),
        // then Thymeleaf can pick it up via ${welcomeMessage} automatically.
        return "home" // render /templates/home.html
    }
}
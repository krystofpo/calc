package com.polansky.amino.controller

import com.polansky.amino.service.LoginResult.*
import com.polansky.amino.service.LoginService
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

const val loginUrl = "login"
const val rootUrl = "api/v1"
const val homeUrl = "home"


@Controller
@RequestMapping("/$rootUrl/auth")
class LoginController(val loginService: LoginService) {


    @GetMapping("/$loginUrl")
    fun showLoginForm(): String {
        return loginUrl
    }

    @PostMapping("/$loginUrl")
    fun processLogin(
        @RequestParam("email") email: String,
        @RequestParam("password") password: String,
        response: HttpServletResponse,
        model: Model,
        redirectAttrs: RedirectAttributes
    ): String {
        val result = loginService.tryLogin(email, password)
        return when (result) {
            OK -> {
                setJwtCookieAndRedirect(response)
            }

            BLANK_EMAIL, BLANK_PASSWORD -> {
                addErrorMessageAndReturnLoginViewName(model, "Email and password must not be blank.")
            }

            UNKNOWN_EMAIL -> {
                addErrorMessageAndReturnLoginViewName(model,"Unknown email.")
            }

            WRONG_PASSWORD -> {
                addErrorMessageAndReturnLoginViewName(model, "Wrong password.")
            }
        }
    }

    private fun setJwtCookieAndRedirect(response: HttpServletResponse): String {
        //TODO dodelat hodnoty do cookie
        val cookie = Cookie("accessToken", "TODO").apply { // cookie name & value
            isHttpOnly = true  // JS can’t read it
            secure = true // only over HTTPS
            path = "/" // valid across your app
            maxAge = 3600   // matches your token lifetime
        }
        cookie.setAttribute("SameSite", "Strict") // CSRF protection
        response.addCookie(cookie)
        return "redirect:/$rootUrl/$homeUrl"
    }

    private fun addErrorMessageAndReturnLoginViewName(model: Model, errorMessage: String): String {
        model.addAttribute("errorMessage", errorMessage)
        return loginUrl
    }

}
package com.polansky.amino.controller

import groovy.json.JsonSlurper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys

import com.polansky.amino.entity.User
import com.polansky.amino.service.LoginResult
import com.polansky.amino.service.LoginService
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Specification

import static com.polansky.amino.service.LoginResult.BLANK_EMAIL
import static com.polansky.amino.service.LoginResult.BLANK_PASSWORD
import static com.polansky.amino.service.LoginResult.UNKNOWN_EMAIL
import static com.polansky.amino.service.LoginResult.WRONG_PASSWORD
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@AutoConfigureMockMvc
@WebMvcTest(LoginController)
class LoginControllerSpec extends Specification {

    @Autowired
    MockMvc mvc

    @SpringBean
    LoginService loginService = Mock()


//    def "when credentials are valid, redirect to home page and set JWT token into cookie"() {
//        given:
//        String email = "user@example.com"
//        String password = "password123"
//
//        loginService.tryLogin(email, password) >> LoginResult.OK
//
//        when:
//        def response = mvc.perform(post("/api/v1/auth/login")
//                .param("email", email)
//                .param("password", password))
//
//        then: "the server responds with a redirect and issues a JWT"
//        def result = response.andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/api/v1/home"))
//                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
//                .andReturn()
//
//        String setCookie = result.response.getHeader(HttpHeaders.SET_COOKIE)
//        setCookie.contains("accessToken=")             // cookie name & value
//        setCookie.contains("HttpOnly")                 // JS can’t read it
//        setCookie.contains("Secure")                   // only over HTTPS
//        setCookie.contains("Max-Age=")             // matches your token lifetime
//        setCookie.contains("Path=/")                   // valid across your app
//    }
//
//    def "when credentials are wrong, render login view with errorMessage"() {
//        given:
//        loginService.tryLogin(email, password) >> loginResult
//
//        when:
//        def response = mvc.perform(post("/api/v1/auth/login")
//                .param("email", email)
//                .param("password", password))
//
//        then: "the login page is re-rendered (status 200), and model contains errorMessage"
//        response.andExpect(status().isOk())
//                .andExpect(view().name("login"))
//                .andExpect(model().attributeExists("errorMessage"))
//
//        where:
//        email              | password   | loginResult
//        ""                 | "password" | BLANK_EMAIL
//        "xxx@example.com"  | "password" | UNKNOWN_EMAIL
//        "user@example.com" | ""         | BLANK_PASSWORD
//        "user@example.com" | "xxx"      | WRONG_PASSWORD
//    }

}

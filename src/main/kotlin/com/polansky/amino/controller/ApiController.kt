package com.polansky.amino.controller


import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

data class Greeting(val message: String)

@RestController
class ApiController {

    @GetMapping("/api/hello")
    fun hello(): Greeting {
        return Greeting("Hello from Spring Boot + Kotlin!")
    }
}

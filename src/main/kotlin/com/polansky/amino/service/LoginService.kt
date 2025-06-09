package com.polansky.amino.service

import com.polansky.amino.entity.User
import com.polansky.amino.service.LoginResult.UNKNOWN_EMAIL
import com.polansky.amino.service.LoginResult.WRONG_PASSWORD
import org.springframework.stereotype.Service

@Service
class LoginService {

    fun tryLogin(email: String, password: String): LoginResult {
        //TODO
        return when {
            email == "krystofpolansky@gmail.com" && password == "password" -> LoginResult.OK
            else -> UNKNOWN_EMAIL

        }
    }
}

enum class LoginResult {
    OK,
    UNKNOWN_EMAIL,
    BLANK_EMAIL,
    WRONG_PASSWORD,
    BLANK_PASSWORD,
}

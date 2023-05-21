package com.example.seabattle.api.model

import com.example.seabattle.R
import java.util.regex.Pattern

data class UserDto(
    private var username: String? = null,
    private var password: String? = null
) {
    companion object {
        // Валидирует данные и возвращает -1, если всё ок
        public fun validate(user: UserDto): Int {
            if (user.username == null || user.username!!.length < 2 || user.username!!.length > 5) {
                return R.string.validationUsernameMessage
            }
            if (user.password == null || !Pattern.matches("[A-Za-z0-9_\\-!@#\$%]+", user.password.toString())) {
                return R.string.validationPasswordMessage
            }
            return -1
        }
    }
}
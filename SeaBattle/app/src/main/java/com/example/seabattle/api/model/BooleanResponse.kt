package com.example.seabattle.api.model

data class BooleanResponse(
    private var response: Boolean? = null,
    private var errorMessage: String? = null
) {
    public fun getResponse(): Boolean {
        if (response == null) {
            return false
        }
        return response as Boolean
    }

    public fun getMessage(): String {
        if (errorMessage == null) {
            return ""
        }
        return errorMessage as String
    }
}
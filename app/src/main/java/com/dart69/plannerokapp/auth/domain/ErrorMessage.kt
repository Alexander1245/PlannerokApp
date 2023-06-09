package com.dart69.plannerokapp.auth.domain

data class ErrorMessage(
    val code: Int,
    val message: String
) {

    override fun toString(): String =
        "$code. $message"
}

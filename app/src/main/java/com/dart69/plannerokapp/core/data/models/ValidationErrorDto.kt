package com.dart69.plannerokapp.core.data.models

data class ValidationErrorDto(
    val loc: String,
    val msg: String,
    val type: String,
): MessageProvider {
    override fun provide(): String = msg
}

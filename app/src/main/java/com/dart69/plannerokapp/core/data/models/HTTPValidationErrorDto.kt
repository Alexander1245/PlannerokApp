package com.dart69.plannerokapp.core.data.models

data class HTTPValidationErrorDto(
    val detail: String,
): MessageProvider {
    override fun provide(): String = detail
}

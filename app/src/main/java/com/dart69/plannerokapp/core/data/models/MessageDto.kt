package com.dart69.plannerokapp.core.data.models

data class MessageDto(
    val message: String
): MessageProvider {
    override fun provide(): String = message
}

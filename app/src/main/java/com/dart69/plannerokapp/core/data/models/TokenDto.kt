package com.dart69.plannerokapp.core.data.models

data class TokenDto(
    val refresh_token: String,
    val access_token: String,
    val user_id: Int,
)

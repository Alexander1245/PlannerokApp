package com.dart69.plannerokapp.core.domain

data class AuthToken(
    val refreshToken: String,
    val accessToken: String,
)

package com.dart69.plannerokapp.auth.domain

data class AuthToken(
    val refreshToken: String,
    val accessToken: String,
)

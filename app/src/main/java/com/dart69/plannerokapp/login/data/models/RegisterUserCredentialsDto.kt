package com.dart69.plannerokapp.login.data.models

data class RegisterUserCredentialsDto(
    val refresh_token: String?,
    val access_token: String?,
    val user_id: Int?,
)
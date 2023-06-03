package com.dart69.plannerokapp.profile.domain.models

data class Profile(
    val id: Int,
    val avatarUrl: String?,
    val phone: String,
    val username: String,
    val city: String?,
    val aboutMe: String?,
    val birthDate: Long?,
    val zodiacSign: Int?,
)

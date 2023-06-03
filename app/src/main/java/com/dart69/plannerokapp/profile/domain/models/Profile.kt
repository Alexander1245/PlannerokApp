package com.dart69.plannerokapp.profile.domain.models

data class Profile(
    val id: Int,
    val phone: String,
    val zodiacSign: Int?,
    val details: ProfileDetails,
)

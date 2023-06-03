package com.dart69.plannerokapp.profile.data.models

data class UpdateRequest(
    val avatar: AvatarRequest?,
    val birthday: String?,
    val city: String?,
    val instagram: String?,
    val name: String?,
    val status: String?,
    val username: String?,
    val vk: String?,
)
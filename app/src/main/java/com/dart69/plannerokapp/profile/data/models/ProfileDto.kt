package com.dart69.plannerokapp.profile.data.models

data class ProfileDto(
    val avatar: String?,
    val avatars: List<AvatarDto>?,
    val birthday: String?,
    val city: String?,
    val completed_task: Int,
    val created: String,
    val id: Int,
    val instagram: String?,
    val last: String?,
    val name: String,
    val online: Boolean,
    val phone: String,
    val status: String?,
    val username: String,
    val vk: String?
)
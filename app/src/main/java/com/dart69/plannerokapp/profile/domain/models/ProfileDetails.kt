package com.dart69.plannerokapp.profile.domain.models

data class ProfileDetails(
    val name: String,
    val username: String,
    val birthdate: Long?,
    val city: String?,
    val vk: String?,
    val instagram: String?,
    val aboutMe: String?,
    val avatarUri: String,
) {

    companion object {
        val EMPTY = ProfileDetails(
            name = "",
            username = "",
            birthdate = null,
            city = null,
            vk = null,
            instagram = null,
            aboutMe = null,
            avatarUri = ""
        )
    }
}

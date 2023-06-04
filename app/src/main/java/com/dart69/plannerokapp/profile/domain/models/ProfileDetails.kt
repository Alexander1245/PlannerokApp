package com.dart69.plannerokapp.profile.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileDetails(
    val name: String,
    val username: String,
    val birthdate: String?,
    val city: String?,
    val vk: String?,
    val instagram: String?,
    val aboutMe: String?,
    val avatarUri: String?,
    val base64: String? = null,
): Parcelable {

    companion object {
        val EMPTY = ProfileDetails(
            name = "",
            username = "",
            birthdate = null,
            city = null,
            vk = null,
            instagram = null,
            aboutMe = null,
            avatarUri = "",
            base64 = null,
        )
    }
}

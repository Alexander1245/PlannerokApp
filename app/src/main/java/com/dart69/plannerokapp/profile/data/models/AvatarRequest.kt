package com.dart69.plannerokapp.profile.data.models

data class AvatarRequest(
    val base_64: String,
    val filename: String
) {

    companion object {
        fun from(base_64: String?, filename: String?): AvatarRequest? =
            if(base_64 == null || filename == null) null else AvatarRequest(base_64, filename)
    }
}
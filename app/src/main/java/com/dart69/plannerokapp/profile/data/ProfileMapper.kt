package com.dart69.plannerokapp.profile.data

import com.dart69.core.mapper.Mapper
import com.dart69.plannerokapp.core.toEpoch
import com.dart69.plannerokapp.profile.data.models.AvatarRequest
import com.dart69.plannerokapp.profile.data.models.ProfileDto
import com.dart69.plannerokapp.profile.data.models.UpdateRequest
import com.dart69.plannerokapp.profile.domain.models.Profile
import com.dart69.plannerokapp.profile.domain.models.ProfileDetails
import com.dart69.plannerokapp.profile.presentation.ZodiacSignProvider
import javax.inject.Inject

interface ProfileMapper : Mapper<ProfileDto, Profile> {

    fun map(details: ProfileDetails, base64: ImageEncoder.BaseData?): UpdateRequest

    class Implementation @Inject constructor(
        private val zodiacSignProvider: ZodiacSignProvider,
    ) : ProfileMapper {
        override fun map(from: ProfileDto): Profile {
            val epoch = from.birthday?.toEpoch()
            val zodiacSign = epoch?.let(zodiacSignProvider::provide)
            return Profile(
                id = from.id,
                phone = from.phone,
                details = ProfileDetails(
                    avatarUri = from.avatar,
                    username = from.username,
                    city = from.city,
                    birthdate = from.birthday,
                    aboutMe = from.status,
                    instagram = from.instagram,
                    name = from.name,
                    vk = from.vk
                ),
                zodiacSign = zodiacSign,
                )
        }

        override fun map(details: ProfileDetails, base64: ImageEncoder.BaseData?): UpdateRequest {
            return UpdateRequest(
                avatar = AvatarRequest.from(base_64 = base64?.base64, filename = base64?.name),
                birthday = details.birthdate,
                city = details.city,
                instagram = details.instagram,
                name = details.name,
                status = details.aboutMe,
                vk = details.vk,
                username = details.username,
            )
        }

    }
}
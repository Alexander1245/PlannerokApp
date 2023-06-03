package com.dart69.plannerokapp.profile.data

import com.dart69.core.mapper.Mapper
import com.dart69.plannerokapp.core.toDateString
import com.dart69.plannerokapp.profile.data.models.AvatarRequest
import com.dart69.plannerokapp.profile.data.models.ProfileDto
import com.dart69.plannerokapp.profile.data.models.UpdateRequest
import com.dart69.plannerokapp.profile.domain.models.Profile
import com.dart69.plannerokapp.profile.domain.models.ProfileDetails
import com.dart69.plannerokapp.profile.presentation.ZodiacSignProvider
import javax.inject.Inject

interface ProfileMapper : Mapper<ProfileDto, Profile> {
    fun updateDetails(dto: ProfileDto, details: ProfileDetails): ProfileDto

    fun toDetails(dto: ProfileDto): ProfileDetails

    fun toRequest(details: ProfileDetails): UpdateRequest

    class Implementation @Inject constructor(
        private val zodiacSignProvider: ZodiacSignProvider,
        private val dateFormatter: DateFormatter,
        private val encoder: ImageEncoder,
    ) : ProfileMapper {
        override fun map(from: ProfileDto): Profile {
            val birthDate = from.birthday?.let(dateFormatter::format)
            val zodiacSign = birthDate?.let(zodiacSignProvider::provide)
            return Profile(
                id = from.id,
                avatarUrl = from.avatar,
                phone = from.phone,
                username = from.username,
                city = from.city,
                zodiacSign = zodiacSign,
                birthDate = birthDate,
                aboutMe = from.status,
            )
        }

        override fun toDetails(dto: ProfileDto): ProfileDetails =
            ProfileDetails(
                name = dto.name,
                username = dto.username,
                birthdate = dto.birthday?.let(dateFormatter::format),
                city = dto.city,
                vk = dto.vk,
                instagram = dto.instagram,
                aboutMe = dto.status,
                avatarUri = dto.avatar.orEmpty(),
            )

        override fun updateDetails(dto: ProfileDto, details: ProfileDetails): ProfileDto =
            dto.copy(
                avatar = details.avatarUri,
                name = details.name,
                vk = details.vk,
                instagram = details.instagram,
                birthday = details.birthdate?.toDateString(),
                city = details.city,
                status = details.aboutMe,
            )

        override fun toRequest(details: ProfileDetails): UpdateRequest {
            val data = encoder.toBase64(details.avatarUri)
            return UpdateRequest(
                avatar = AvatarRequest.from(base_64 = data.base64, filename = data.name),
                birthday = details.birthdate?.toDateString(),
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
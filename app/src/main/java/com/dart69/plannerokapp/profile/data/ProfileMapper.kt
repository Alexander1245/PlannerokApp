package com.dart69.plannerokapp.profile.data

import com.dart69.core.mapper.Mapper
import com.dart69.plannerokapp.profile.data.models.ProfileDto
import com.dart69.plannerokapp.profile.domain.models.Profile
import com.dart69.plannerokapp.profile.presentation.ZodiacSignProvider
import javax.inject.Inject

interface ProfileMapper : Mapper<ProfileDto, Profile> {

    class Implementation @Inject constructor(
        private val zodiacSignProvider: ZodiacSignProvider,
        private val dateFormatter: DateFormatter,
    ): ProfileMapper {
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
    }
}
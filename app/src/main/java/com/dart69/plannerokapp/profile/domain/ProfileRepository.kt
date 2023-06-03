package com.dart69.plannerokapp.profile.domain

import com.dart69.plannerokapp.profile.domain.models.Profile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(): Flow<Profile>

    suspend fun initialize()

}
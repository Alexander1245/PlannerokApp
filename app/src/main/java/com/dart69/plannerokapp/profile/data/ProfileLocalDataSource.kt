package com.dart69.plannerokapp.profile.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dart69.plannerokapp.profile.domain.models.Profile
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface ProfileLocalDataSource {
    fun observeProfile(): Flow<Profile>

    suspend fun saveProfile(profile: Profile)

    suspend fun isInitialized(): Boolean

    suspend fun update(updater: suspend (Profile) -> Profile)

    class Implementation @Inject constructor(
        private val dataStore: DataStore<Preferences>,
        private val gson: Gson,
    ) : ProfileLocalDataSource {
        override suspend fun isInitialized(): Boolean =
            PROFILE_KEY in dataStore.data.first()

        override fun observeProfile(): Flow<Profile> =
            dataStore.data.map { preferences ->
                gson.fromJson(preferences[PROFILE_KEY], Profile::class.java)
            }

        override suspend fun saveProfile(profile: Profile) {
            dataStore.edit { preferences ->
                preferences[PROFILE_KEY] = gson.toJson(profile)
            }
        }

        override suspend fun update(updater: suspend (Profile) -> Profile) {
            dataStore.edit { preferences ->
                val old = gson.fromJson(preferences[PROFILE_KEY], Profile::class.java)
                preferences[PROFILE_KEY] = gson.toJson(updater(old))
            }
        }

        private companion object {
            val PROFILE_KEY = stringPreferencesKey("profile")
        }
    }
}
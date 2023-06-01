package com.dart69.plannerokapp.core.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.dart69.plannerokapp.core.domain.AuthToken

interface AuthLocalDataSource {
    suspend fun loadAuthToken(): AuthToken?

    suspend fun saveAuthToken(token: AuthToken)

    class Implementation(
        private val sharedPreferences: SharedPreferences
    ) : AuthLocalDataSource {
        override suspend fun loadAuthToken(): AuthToken? {
            val refreshToken = sharedPreferences.getString(REFRESH_KEY, null)
            val accessToken = sharedPreferences.getString(ACCESS_KEY, null)
            return if (refreshToken != null && accessToken != null)
                AuthToken(refreshToken, accessToken) else null
        }

        override suspend fun saveAuthToken(token: AuthToken) =
            sharedPreferences.edit {
                putString(REFRESH_KEY, token.refreshToken)
                putString(ACCESS_KEY, token.accessToken)
            }

        private companion object {
            const val REFRESH_KEY = "refresh_token"
            const val ACCESS_KEY = "access_token"
        }
    }
}
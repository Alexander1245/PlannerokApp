package com.dart69.plannerokapp.login.data

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import javax.inject.Inject

interface LoginLocalDataSource {
    suspend fun setUserLogged(isLoggedIn: Boolean)

    suspend fun loginUser()

    suspend fun isLoggedIn(): Boolean

    class Implementation @Inject constructor(
        private val sharedPreferences: SharedPreferences
    ): LoginLocalDataSource {

        override suspend fun setUserLogged(isLoggedIn: Boolean) {
            sharedPreferences.edit { putBoolean(PREFS_KEY, isLoggedIn) }
        }

        override suspend fun loginUser() {
            setUserLogged(true)
        }

        override suspend fun isLoggedIn(): Boolean =
            sharedPreferences.getBoolean(PREFS_KEY, false)

        private companion object {
            const val PREFS_KEY = "is_user_logged_in"
        }
    }
}
package com.dart69.plannerokapp.core.data

import com.dart69.plannerokapp.auth.data.AuthLocalDataSource
import com.dart69.plannerokapp.auth.domain.AuthToken

class FakeAuthLocalSource : AuthLocalDataSource {
    @Volatile
    private var token: AuthToken? = null

    override suspend fun loadAuthToken(): AuthToken? = token

    override suspend fun saveAuthToken(token: AuthToken) {
        this.token = token
    }
}
package com.dart69.plannerokapp.login.data

import com.dart69.plannerokapp.core.data.FakeTokenChecker
import com.dart69.plannerokapp.auth.domain.AuthRepository
import com.dart69.plannerokapp.auth.domain.AuthToken

class FakeAuthRepo : AuthRepository {
    @Volatile
    var token = FakeTokenChecker.EXPIRED_TOKEN

    override suspend fun loadTokens(): AuthToken = token

    override suspend fun updateTokens(token: AuthToken) {
        this.token = token
    }
}
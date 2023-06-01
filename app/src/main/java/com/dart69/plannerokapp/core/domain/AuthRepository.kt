package com.dart69.plannerokapp.core.domain

interface AuthRepository {
    suspend fun loadTokens(): AuthToken

    suspend fun updateTokens(token: AuthToken)
}
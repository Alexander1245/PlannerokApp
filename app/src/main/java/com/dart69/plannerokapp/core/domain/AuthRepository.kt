package com.dart69.plannerokapp.core.domain

import com.dart69.plannerokapp.core.data.ApiException

interface AuthRepository {
    suspend fun loadTokens(): AuthToken

    suspend fun updateTokens(token: AuthToken)
}

suspend fun <T> AuthRepository.useActualToken(block: suspend (AuthToken) -> T): T =
    try {
        block(loadTokens())
    } catch (exception: ApiException) {
        if (exception.errorMessage.code == 401) {
            block(loadTokens())
        } else {
            throw exception
        }
    }
package com.dart69.plannerokapp.auth.data

import com.dart69.plannerokapp.auth.domain.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class JWTAuthenticator @Inject constructor(
    private val authRepository: AuthRepository.Holder,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (response.header(HEADER_NAME) != null) return null
        val accessToken = runBlocking { authRepository.loadTokens().accessToken }
        return response.request.newBuilder()
            .header(HEADER_NAME, "$TOKEN_TYPE $accessToken")
            .build()
    }

    private companion object {
        const val HEADER_NAME = "Authorization"
        const val TOKEN_TYPE = "Bearer"
    }
}
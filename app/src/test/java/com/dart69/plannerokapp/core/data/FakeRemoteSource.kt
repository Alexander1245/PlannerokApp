package com.dart69.plannerokapp.core.data

import com.dart69.plannerokapp.core.data.models.TokenDto
import retrofit2.Response
import java.util.concurrent.atomic.AtomicInteger

class FakeRemoteSource : AuthRemoteDataSource {
    val calls = AtomicInteger(0)

    override suspend fun refreshToken(refreshToken: String): Response<TokenDto> =
        Response.success(
            TokenDto(
                FakeTokenChecker.NORMAL_TOKEN.refreshToken,
                FakeTokenChecker.NORMAL_TOKEN.accessToken,
                0
            )
        ).also { calls.incrementAndGet() }
}
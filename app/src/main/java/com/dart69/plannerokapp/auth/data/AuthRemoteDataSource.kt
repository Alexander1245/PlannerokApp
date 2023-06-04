package com.dart69.plannerokapp.auth.data

import com.dart69.plannerokapp.auth.data.models.RefreshTokenRequest
import com.dart69.plannerokapp.auth.data.models.TokenDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRemoteDataSource {

    @POST("/api/v1/users/refresh-token/")
    suspend fun refreshToken(
        @Body body: RefreshTokenRequest
    ): Response<TokenDto>
}
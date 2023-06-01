package com.dart69.plannerokapp.core.data

import com.dart69.plannerokapp.core.data.models.TokenDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AuthRemoteDataSource {

    @GET("/api/v1/users/refresh-token/")
    suspend fun refreshToken(
        @Query("refresh_token") refreshToken: String
    ): Response<TokenDto>
}
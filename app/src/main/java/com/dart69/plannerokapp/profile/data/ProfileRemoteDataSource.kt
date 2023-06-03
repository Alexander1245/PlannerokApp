package com.dart69.plannerokapp.profile.data

import com.dart69.plannerokapp.profile.data.models.AvatarResponse
import com.dart69.plannerokapp.profile.data.models.UpdateRequest
import com.dart69.plannerokapp.profile.data.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileRemoteDataSource {

    @GET("/api/v1/users/me/")
    suspend fun refreshProfile(): Response<UserResponse>

    @PUT("/api/v1/users/me/")
    suspend fun updateProfile(
        @Body body: UpdateRequest,
    ): Response<AvatarResponse>
}
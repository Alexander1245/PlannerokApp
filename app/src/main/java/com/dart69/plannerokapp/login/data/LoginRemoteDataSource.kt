package com.dart69.plannerokapp.login.data

import com.dart69.plannerokapp.login.data.models.LoginDto
import com.dart69.plannerokapp.login.data.models.IsSuccessDto
import com.dart69.plannerokapp.login.data.models.PhoneDto
import com.dart69.plannerokapp.login.data.models.RegisterDto
import com.dart69.plannerokapp.login.data.models.LoginUserCredentialsDto
import com.dart69.plannerokapp.login.data.models.RegisterUserCredentialsDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRemoteDataSource {

    @POST("/api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(
        @Body body: PhoneDto
    ): Response<IsSuccessDto>

    @POST("/api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(
        @Body body: LoginDto
    ): Response<LoginUserCredentialsDto>

    @POST("/api/v1/users/register/")
    suspend fun register(
        @Body body: RegisterDto
    ): Response<RegisterUserCredentialsDto>
}
package com.dart69.plannerokapp.login.data

import com.dart69.plannerokapp.core.data.FakeTokenChecker
import com.dart69.plannerokapp.login.data.models.IsSuccessDto
import com.dart69.plannerokapp.login.data.models.LoginDto
import com.dart69.plannerokapp.login.data.models.LoginUserCredentialsDto
import com.dart69.plannerokapp.login.data.models.PhoneDto
import com.dart69.plannerokapp.login.data.models.RegisterDto
import com.dart69.plannerokapp.login.data.models.RegisterUserCredentialsDto
import retrofit2.Response

class FakeLoginRemoteDataSource : LoginRemoteDataSource {
    companion object {
        const val CORRECT_AUTH_CODE = 123
    }

    @Volatile
    var willCodeBeSent = false

    @Volatile
    var isUserExists = false

    override suspend fun sendAuthCode(body: PhoneDto): Response<IsSuccessDto> =
        Response.success(IsSuccessDto(willCodeBeSent))

    override suspend fun checkAuthCode(body: LoginDto): Response<LoginUserCredentialsDto> {
        fun provide(field: String): String? = if (isUserExists) field else null

        require(body.code == CORRECT_AUTH_CODE)

        return Response.success(
            LoginUserCredentialsDto(
                refresh_token = provide(FakeTokenChecker.NORMAL_TOKEN.refreshToken),
                access_token = provide(FakeTokenChecker.NORMAL_TOKEN.accessToken),
                user_id = provide("1")?.toInt(),
                is_user_exists = isUserExists,
            )
        )
    }

    override suspend fun register(body: RegisterDto): Response<RegisterUserCredentialsDto> =
        Response.success(
            RegisterUserCredentialsDto(
                FakeTokenChecker.NORMAL_TOKEN.refreshToken,
                FakeTokenChecker.NORMAL_TOKEN.accessToken,
                1,
            )
        )
}
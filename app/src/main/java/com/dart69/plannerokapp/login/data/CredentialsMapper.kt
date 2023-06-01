package com.dart69.plannerokapp.login.data

import com.dart69.core.mapper.Mapper
import com.dart69.plannerokapp.core.domain.AuthToken
import com.dart69.plannerokapp.login.data.models.LoginUserCredentialsDto
import com.dart69.plannerokapp.login.data.models.RegisterUserCredentialsDto

interface CredentialsMapper : Mapper<LoginUserCredentialsDto, AuthToken?> {
    fun map(from: RegisterUserCredentialsDto): AuthToken

    companion object: CredentialsMapper {
        override fun map(from: LoginUserCredentialsDto): AuthToken? =
            from.let { dto ->
                AuthToken(
                    refreshToken = dto.refresh_token ?: return@let null,
                    accessToken = dto.access_token ?: return@let null,
                )
            }

        override fun map(from: RegisterUserCredentialsDto): AuthToken =
            AuthToken(requireNotNull(from.refresh_token), requireNotNull(from.access_token))
    }
}
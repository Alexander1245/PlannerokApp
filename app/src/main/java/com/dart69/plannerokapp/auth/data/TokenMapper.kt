package com.dart69.plannerokapp.auth.data

import com.dart69.core.mapper.Mapper
import com.dart69.plannerokapp.auth.data.models.TokenDto
import com.dart69.plannerokapp.auth.domain.AuthToken

fun interface TokenMapper : Mapper<TokenDto, AuthToken> {

    companion object : TokenMapper {
        override fun map(from: TokenDto): AuthToken =
            AuthToken(
                refreshToken = from.refresh_token,
                accessToken = from.access_token,
            )
    }
}
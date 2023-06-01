package com.dart69.plannerokapp.core.data

import com.dart69.core.mapper.Mapper
import com.dart69.plannerokapp.core.data.models.TokenDto
import com.dart69.plannerokapp.core.domain.AuthToken

fun interface TokenMapper : Mapper<TokenDto, AuthToken> {

    companion object : TokenMapper {
        override fun map(from: TokenDto): AuthToken =
            AuthToken(
                refreshToken = from.refresh_token,
                accessToken = from.access_token,
            )
    }
}
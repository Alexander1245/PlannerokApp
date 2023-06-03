package com.dart69.plannerokapp.auth.data

import arrow.core.getOrElse
import com.dart69.plannerokapp.auth.domain.AuthToken
import io.github.nefilim.kjwt.JWT
import java.time.Instant
import javax.inject.Inject

interface TokenChecker {
    fun isExpired(token: AuthToken): Boolean

    class Implementation @Inject constructor() : TokenChecker {
        override fun isExpired(token: AuthToken): Boolean =
            JWT.decode(token.accessToken).map {
                val now = Instant.now()
                val exp = it.expiresAt().getOrElse { now }
                now.toEpochMilli() >= exp.toEpochMilli()
            }.getOrElse { true }
    }
}
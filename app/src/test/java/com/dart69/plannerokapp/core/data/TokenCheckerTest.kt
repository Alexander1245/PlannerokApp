package com.dart69.plannerokapp.core.data

import com.dart69.plannerokapp.core.domain.AuthToken
import io.github.nefilim.kjwt.JWT
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.Instant

internal class TokenCheckerTest {
    private lateinit var checker: TokenChecker.Implementation

    @Before
    fun beforeEach() {
        checker = TokenChecker.Implementation()
    }

    @Test
    fun `if an access token is expired then the checker returns true`() {
        val expiredToken = JWT.hs256 {
            val now = Instant.now()
            issuedAt(now)
            expiresAt(Instant.ofEpochMilli(now.toEpochMilli() - 66666L))
        }.encode()
        assertTrue(checker.isExpired(AuthToken(refreshToken = "", accessToken = expiredToken)))
    }

    @Test
    fun `if an access token is not expired then the checker returns false`() {
        val normalToken = JWT.hs256 {
            val now = Instant.now()
            issuedAt(now)
            expiresAt(Instant.ofEpochMilli(now.toEpochMilli() + 66666L))
        }.encode()
        assertFalse(checker.isExpired(AuthToken(refreshToken = "", accessToken = normalToken)))
    }
}
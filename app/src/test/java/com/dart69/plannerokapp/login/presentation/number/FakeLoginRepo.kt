package com.dart69.plannerokapp.login.presentation.number

import com.dart69.plannerokapp.login.domain.LoginRepository
import java.util.concurrent.atomic.AtomicInteger

class FakeLoginRepo : LoginRepository {
    @Volatile
    var isExists = false

    @Volatile
    var willCodeBeSent = true

    val sendCalls = AtomicInteger(0)

    override suspend fun sendAuthCode(phone: String): Boolean = willCodeBeSent.also {
        sendCalls.incrementAndGet()
    }

    override suspend fun verifyIsUserExists(phone: String, authCode: Int): Boolean = isExists

    override suspend fun register(phone: String, name: String, username: String) {}
    override suspend fun isLoggedIn(): Boolean = isExists

    override fun checkCodeValidity(authCode: Int): Boolean = true
}
package com.dart69.plannerokapp.login.domain

interface LoginRepository {
    suspend fun sendAuthCode(phone: String)

    suspend fun verifyAuthCode(authCode: String)

    suspend fun login(phone: String, code: String)

    suspend fun register(phone: String, code: String)
}
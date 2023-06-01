package com.dart69.plannerokapp.login.data

import com.dart69.plannerokapp.login.domain.LoginRepository

class LoginRepositoryImpl : LoginRepository {
    override suspend fun sendAuthCode(phone: String) {
        TODO("Not yet implemented")
    }

    override suspend fun verifyAuthCode(authCode: String) {
        TODO("Not yet implemented")
    }

    override suspend fun login(phone: String, code: String) {
        TODO("Not yet implemented")
    }

    override suspend fun register(phone: String, code: String) {
        TODO("Not yet implemented")
    }
}
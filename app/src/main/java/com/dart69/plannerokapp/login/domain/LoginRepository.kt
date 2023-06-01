package com.dart69.plannerokapp.login.domain

interface LoginRepository {

    /**
     * @return true if the code was successfully send, false otherwise
     * */
    suspend fun sendAuthCode(phone: String): Boolean

    /**
     * @return true if the user is registered, false otherwise
     * */
    suspend fun verifyIsUserExists(authCode: Int): Boolean

    suspend fun register(phone: String, name: String, username: String)
}
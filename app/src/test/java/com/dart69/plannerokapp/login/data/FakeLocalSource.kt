package com.dart69.plannerokapp.login.data

internal class FakeLocalSource : LoginLocalDataSource {
    @Volatile
    var isLoggedIn = false

    override suspend fun setUserLogged(isLoggedIn: Boolean) {}

    override suspend fun loginUser() {}

    override suspend fun isLoggedIn(): Boolean = isLoggedIn
}
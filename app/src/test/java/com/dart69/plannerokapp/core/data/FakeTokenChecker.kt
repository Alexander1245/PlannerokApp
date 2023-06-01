package com.dart69.plannerokapp.core.data

import com.dart69.plannerokapp.core.domain.AuthToken

class FakeTokenChecker : TokenChecker {

    override fun isExpired(token: AuthToken): Boolean =
        when(token) {
            EXPIRED_TOKEN -> true
            NORMAL_TOKEN -> false
            else -> error("UNKNOWN TOKEN!!!")
        }

    companion object {
        val EXPIRED_TOKEN = AuthToken("a", "b")
        val NORMAL_TOKEN = AuthToken("ab", "ba")
    }
}
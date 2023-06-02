package com.dart69.plannerokapp.core.domain

interface AuthRepository {
    suspend fun loadTokens(): AuthToken

    suspend fun updateTokens(token: AuthToken)

    /**
     * Avoid cyclic dependencies. Bruh
     * */
    object Holder : AuthRepository {
        private lateinit var repository: AuthRepository
        fun initialize(repository: AuthRepository) {
            this.repository = repository
        }

        override suspend fun loadTokens(): AuthToken = repository.loadTokens()

        override suspend fun updateTokens(token: AuthToken) = repository.updateTokens(token)
    }
}
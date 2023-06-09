package com.dart69.plannerokapp.auth.data

import com.dart69.core.coroutines.DispatchersProvider
import com.dart69.data.response.wrapper.ResponseWrapper
import com.dart69.plannerokapp.auth.data.models.RefreshTokenRequest
import com.dart69.plannerokapp.auth.domain.AuthRepository
import com.dart69.plannerokapp.auth.domain.AuthToken
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val localDataSource: AuthLocalDataSource,
    private val remoteDataSource: AuthRemoteDataSource,
    private val responseWrapper: ResponseWrapper,
    private val tokenMapper: TokenMapper,
    private val tokenChecker: TokenChecker,
    private val dispatchers: DispatchersProvider,
) : AuthRepository {
    private val mutex = Mutex()

    override suspend fun loadTokens(): AuthToken =
        withContext(dispatchers.provideBackgroundDispatcher()) {
            mutex.withLock {
                val previousToken = localDataSource.loadAuthToken() ?: error(ERROR_MESSAGE)
                if (tokenChecker.isExpired(previousToken)) {
                    val refreshToken = previousToken.refreshToken
                    val dto = responseWrapper.wrap {
                        remoteDataSource.refreshToken(RefreshTokenRequest(refreshToken))
                    }
                    tokenMapper.map(dto).also { localDataSource.saveAuthToken(it) }
                } else {
                    previousToken
                }
            }
        }

    override suspend fun updateTokens(token: AuthToken) =
        withContext(dispatchers.provideBackgroundDispatcher()) {
            mutex.withLock {
                localDataSource.saveAuthToken(token)
            }
        }

    private companion object {
        const val ERROR_MESSAGE = "Token must be initialized"
    }
}
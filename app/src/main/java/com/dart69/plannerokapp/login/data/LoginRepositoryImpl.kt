package com.dart69.plannerokapp.login.data

import com.dart69.core.coroutines.DispatchersProvider
import com.dart69.data.response.wrapper.ResponseWrapper
import com.dart69.plannerokapp.auth.domain.AuthRepository
import com.dart69.plannerokapp.core.length
import com.dart69.plannerokapp.login.data.models.LoginDto
import com.dart69.plannerokapp.login.data.models.PhoneDto
import com.dart69.plannerokapp.login.data.models.RegisterDto
import com.dart69.plannerokapp.login.domain.LoginRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val dispatchers: DispatchersProvider,
    private val remoteDataSource: LoginRemoteDataSource,
    private val localDataSource: LoginLocalDataSource,
    private val responseWrapper: ResponseWrapper,
    private val authRepository: AuthRepository,
    private val mapper: CredentialsMapper,
) : LoginRepository {
    override suspend fun sendAuthCode(phone: String): Boolean =
        withContext(dispatchers.provideBackgroundDispatcher()) {
            responseWrapper.wrap {
                remoteDataSource.sendAuthCode(PhoneDto(phone))
            }.is_success
        }

    override suspend fun isLoggedIn(): Boolean = localDataSource.isLoggedIn()

    override suspend fun verifyIsUserExists(phone: String, authCode: Int): Boolean =
        withContext(dispatchers.provideBackgroundDispatcher()) {
            val credentials = responseWrapper.wrap {
                remoteDataSource.checkAuthCode(LoginDto(phone, authCode))
            }
            mapper.map(credentials)?.let { token ->
                authRepository.updateTokens(token)
            }
            credentials.is_user_exists.also { localDataSource.setUserLogged(it) }
        }

    override suspend fun register(phone: String, name: String, username: String) =
        withContext(dispatchers.provideBackgroundDispatcher()) {
            val credentials = responseWrapper.wrap {
                remoteDataSource.register(RegisterDto(phone, name, username))
            }
            authRepository.updateTokens(requireNotNull(mapper.map(credentials)))
            localDataSource.loginUser()
        }

    override fun checkCodeValidity(authCode: Int): Boolean =
        authCode.length == LoginRepository.CODE_LENGTH
}
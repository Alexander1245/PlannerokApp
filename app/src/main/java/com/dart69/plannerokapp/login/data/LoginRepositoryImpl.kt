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
    private val cachedDataSource: LoginCachedDataSource,
    private val responseWrapper: ResponseWrapper,
    private val authRepository: AuthRepository,
    private val mapper: CredentialsMapper,
) : LoginRepository {
    override suspend fun sendAuthCode(phone: String): Boolean =
        withContext(dispatchers.provideBackgroundDispatcher()) {
            cachedDataSource.savePhone(phone)
            responseWrapper.wrap {
                remoteDataSource.sendAuthCode(PhoneDto(phone))
            }.is_success
        }

    override suspend fun verifyIsUserExists(authCode: Int): Boolean =
        withContext(dispatchers.provideBackgroundDispatcher()) {
            val credentials = responseWrapper.wrap {
                remoteDataSource.checkAuthCode(
                    LoginDto(
                        cachedDataSource.loadPhone()
                            ?: error("You must call sendAuthCode before verification"),
                        authCode,
                    )
                )
            }
            mapper.map(credentials)?.let { token ->
                authRepository.updateTokens(token)
            }
            credentials.is_user_exists
        }

    override suspend fun register(phone: String, name: String, username: String) =
        withContext(dispatchers.provideBackgroundDispatcher()) {
            val credentials = responseWrapper.wrap {
                remoteDataSource.register(RegisterDto(phone, name, username))
            }
            authRepository.updateTokens(requireNotNull(mapper.map(credentials)))
        }

    override fun checkCodeValidity(authCode: Int): Boolean =
        authCode.length == LoginRepository.CODE_LENGTH
}
package com.dart69.plannerokapp.profile.data

import com.dart69.core.coroutines.DispatchersProvider
import com.dart69.data.response.wrapper.ResponseWrapper
import com.dart69.plannerokapp.profile.domain.ProfileRepository
import com.dart69.plannerokapp.profile.domain.models.Profile
import com.dart69.plannerokapp.profile.domain.models.ProfileDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val dispatchers: DispatchersProvider,
    private val remoteDataSource: ProfileRemoteDataSource,
    private val localDataSource: ProfileLocalDataSource,
    private val responseWrapper: ResponseWrapper,
    private val mapper: ProfileMapper,
    private val encoder: ImageEncoder,
) : ProfileRepository {

    override fun observeProfile(): Flow<Profile> =
        localDataSource.observeProfile()
            .flowOn(dispatchers.provideBackgroundDispatcher())

    override suspend fun getProfileDetails(): ProfileDetails =
        withContext(dispatchers.provideBackgroundDispatcher()) {
            localDataSource.observeProfile().first().details
        }

    override suspend fun initialize() {
        if (localDataSource.isInitialized()) return
        withContext(dispatchers.provideBackgroundDispatcher()) {
            val dto = responseWrapper.wrap { remoteDataSource.refreshProfile() }.profile_data
            localDataSource.saveProfile(mapper.map(dto))
        }
    }

    override suspend fun updateProfileDetails(details: ProfileDetails) {
        val base64 = encoder.toBase64(details.avatarUri.orEmpty())
        responseWrapper.wrap {
            remoteDataSource.updateProfile(mapper.map(details, base64))
        }
        localDataSource.update { profile ->
            profile.copy(
                details = details.copy(avatarUri = details.avatarUri, base64 = base64.base64),
            )
        }
    }
}
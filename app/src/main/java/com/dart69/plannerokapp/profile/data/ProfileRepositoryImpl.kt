package com.dart69.plannerokapp.profile.data

import com.dart69.core.coroutines.DispatchersProvider
import com.dart69.data.response.wrapper.ResponseWrapper
import com.dart69.plannerokapp.profile.domain.ProfileRepository
import com.dart69.plannerokapp.profile.domain.models.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val dispatchers: DispatchersProvider,
    private val remoteDataSource: ProfileRemoteDataSource,
    private val localDataSource: ProfileLocalDataSource,
    private val responseWrapper: ResponseWrapper,
    private val mapper: ProfileMapper,
) : ProfileRepository {

    override fun observeProfile(): Flow<Profile> =
        localDataSource.observeProfile()
            .map(mapper::map)
            .flowOn(dispatchers.provideBackgroundDispatcher())

    override suspend fun initialize() {
        if (localDataSource.isInitialized()) return
        withContext(dispatchers.provideBackgroundDispatcher()) {
            val profile = responseWrapper.wrap { remoteDataSource.refreshProfile() }.profile_data
            localDataSource.saveProfile(profile)
        }
    }
}
package com.dart69.plannerokapp.login.di

import com.dart69.data.response.wrapper.ResponseWrapper
import com.dart69.plannerokapp.core.data.ResponseWrapperImpl
import com.dart69.plannerokapp.login.data.CredentialsMapper
import com.dart69.plannerokapp.login.data.LoginLocalDataSource
import com.dart69.plannerokapp.login.data.LoginRemoteDataSource
import com.dart69.plannerokapp.login.data.LoginRepositoryImpl
import com.dart69.plannerokapp.login.domain.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Provides
    fun provideLoginRemoteSource(retrofit: Retrofit): LoginRemoteDataSource = retrofit.create()

    @Provides
    fun provideCachedDataSource(
        impl: LoginLocalDataSource.Implementation
    ): LoginLocalDataSource = impl

    @Provides
    fun provideResponseWrapper(): ResponseWrapper = ResponseWrapperImpl()

    @Provides
    fun provideMapper(): CredentialsMapper = CredentialsMapper

    @Provides
    @Singleton
    fun provideRepository(impl: LoginRepositoryImpl): LoginRepository = impl
}
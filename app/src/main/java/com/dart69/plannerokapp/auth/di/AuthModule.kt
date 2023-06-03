package com.dart69.plannerokapp.auth.di

import com.dart69.plannerokapp.auth.data.AuthLocalDataSource
import com.dart69.plannerokapp.auth.data.AuthRemoteDataSource
import com.dart69.plannerokapp.auth.data.AuthRepositoryImpl
import com.dart69.plannerokapp.auth.data.JWTAuthenticator
import com.dart69.plannerokapp.auth.data.TokenChecker
import com.dart69.plannerokapp.auth.data.TokenMapper
import com.dart69.plannerokapp.auth.domain.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {
    @Provides
    fun provideLocalDataSource(
        impl: AuthLocalDataSource.Implementation
    ): AuthLocalDataSource = impl

    @Provides
    fun provideRemoteDataSource(retrofit: Retrofit): AuthRemoteDataSource = retrofit.create()

    @Provides
    fun provideTokenMapper(): TokenMapper = TokenMapper

    @Provides
    fun provideTokenChecker(impl: TokenChecker.Implementation): TokenChecker = impl

    @Provides
    @Singleton
    fun provideRepository(impl: AuthRepositoryImpl): AuthRepository = impl.also { repository ->
        AuthRepository.Holder.initialize(repository)
    }

    @Provides
    fun provideHolder(): AuthRepository.Holder = AuthRepository.Holder

    @Provides
    fun provideAuthenticator(impl: JWTAuthenticator): Authenticator = impl
}
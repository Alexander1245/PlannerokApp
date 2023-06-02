package com.dart69.plannerokapp.core.di

import android.content.Context
import android.content.SharedPreferences
import com.dart69.core.coroutines.DispatchersProvider
import com.dart69.plannerokapp.core.data.AuthLocalDataSource
import com.dart69.plannerokapp.core.data.AuthRemoteDataSource
import com.dart69.plannerokapp.core.data.AuthRepositoryImpl
import com.dart69.plannerokapp.core.data.JWTAuthenticator
import com.dart69.plannerokapp.core.data.TokenChecker
import com.dart69.plannerokapp.core.data.TokenMapper
import com.dart69.plannerokapp.core.domain.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {
    private const val PREFS_NAME = "shared_prefs"

    @Provides
    fun provideDispatchers(): DispatchersProvider = DispatchersProvider()

    @Provides
    fun provideOkHttpClient(
        authenticator: Authenticator,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .authenticator(authenticator)
            .addNetworkInterceptor {
                it.proceed(
                    it.request()
                        .newBuilder()
                        .addHeader("accept", "application/json")
                        .addHeader("Content-Type", "application/json")
                        .build()
                )
            }
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://plannerok.ru")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

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
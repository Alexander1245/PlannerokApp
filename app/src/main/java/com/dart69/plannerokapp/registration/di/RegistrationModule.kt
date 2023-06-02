package com.dart69.plannerokapp.registration.di

import com.dart69.plannerokapp.registration.domain.UserCredentialsMatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RegistrationModule {

    @Provides
    fun provideMatcher(): UserCredentialsMatcher = UserCredentialsMatcher.Implementation(
        UserCredentialsMatcher.AllowedCharacters(
            letters = 26,
            digits = 10,
            underscores = 2,
        )
    )
}
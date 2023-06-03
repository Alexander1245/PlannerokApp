package com.dart69.plannerokapp.profile.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dart69.plannerokapp.profile.data.DateFormatter
import com.dart69.plannerokapp.profile.data.ImageEncoder
import com.dart69.plannerokapp.profile.data.ProfileLocalDataSource
import com.dart69.plannerokapp.profile.data.ProfileMapper
import com.dart69.plannerokapp.profile.data.ProfileRemoteDataSource
import com.dart69.plannerokapp.profile.data.ProfileRepositoryImpl
import com.dart69.plannerokapp.profile.domain.ProfileRepository
import com.dart69.plannerokapp.profile.presentation.ZodiacSignProvider
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {
    private val gson = Gson()
    private const val JPEG_QUALITY = 95
    private const val PREFS_NAME = "profile_prefs"
    private val Context.dataStore by preferencesDataStore(PREFS_NAME)

    @Provides
    fun provideGson(): Gson = gson

    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Provides
    fun provideSignProvider(impl: ZodiacSignProvider.Implementation): ZodiacSignProvider = impl

    @Provides
    fun provideDateFormatter(): DateFormatter = DateFormatter.Implementation()

    @Provides
    fun provideMapper(impl: ProfileMapper.Implementation): ProfileMapper = impl

    @Provides
    fun provideEncoder(
        @ApplicationContext context: Context,
    ): ImageEncoder = ImageEncoder.Implementation(JPEG_QUALITY, context)

    @Provides
    fun provideLocalDataSource(
        impl: ProfileLocalDataSource.Implementation
    ): ProfileLocalDataSource = impl

    @Provides
    fun provideRemoteDataSource(retrofit: Retrofit): ProfileRemoteDataSource = retrofit.create()

    @Provides
    fun provideRepository(impl: ProfileRepositoryImpl): ProfileRepository = impl
}
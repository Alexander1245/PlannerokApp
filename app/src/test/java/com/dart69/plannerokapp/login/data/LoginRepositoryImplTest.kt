package com.dart69.plannerokapp.login.data

import com.dart69.core.coroutines.DispatchersProvider
import com.dart69.plannerokapp.core.data.FakeTokenChecker
import com.dart69.plannerokapp.core.data.ResponseWrapperImpl
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class LoginRepositoryImplTest {
    private lateinit var repository: LoginRepositoryImpl
    private lateinit var fakeAuthRepo: FakeAuthRepo
    private lateinit var localSource: LoginLocalDataSource
    private lateinit var fakeRemoteSource: FakeLoginRemoteDataSource

    @Before
    fun beforeEach() {
        fakeAuthRepo = FakeAuthRepo()
        fakeRemoteSource = FakeLoginRemoteDataSource()
        localSource = FakeLocalSource()
        repository = LoginRepositoryImpl(
            dispatchers = DispatchersProvider(),
            remoteDataSource = fakeRemoteSource,
            localDataSource = localSource,
            responseWrapper = ResponseWrapperImpl(),
            authRepository = fakeAuthRepo,
            mapper = CredentialsMapper.Companion,
        )
    }

    @Test
    fun `sendAuthCode returns true if success`() = runBlocking {
        fakeRemoteSource.willCodeBeSent = true
        assertTrue(repository.sendAuthCode("123"))
    }

    @Test
    fun `sendAuthCode returns false if not success`() = runBlocking {
        fakeRemoteSource.willCodeBeSent = false
        assertFalse(repository.sendAuthCode("123"))
    }

    @Test
    fun `if the user is exists tokens will be saved`() = runBlocking {
        `sendAuthCode returns true if success`()
        fakeRemoteSource.isUserExists = true
        assertTrue(repository.verifyIsUserExists("123", FakeLoginRemoteDataSource.CORRECT_AUTH_CODE))
        assertEquals(FakeTokenChecker.NORMAL_TOKEN, fakeAuthRepo.loadTokens())
    }

    @Test
    fun `if the user isn't exists tokens will not be saved`() = runBlocking {
        `sendAuthCode returns true if success`()
        fakeRemoteSource.isUserExists = false
        assertFalse(repository.verifyIsUserExists("123", FakeLoginRemoteDataSource.CORRECT_AUTH_CODE))
        assertEquals(FakeTokenChecker.EXPIRED_TOKEN, fakeAuthRepo.loadTokens())
    }

    @Test
    fun `register save tokens`() = runBlocking {
        assertEquals(FakeTokenChecker.EXPIRED_TOKEN, fakeAuthRepo.loadTokens())
        repository.register("234", "Name", "Username")
        assertEquals(FakeTokenChecker.NORMAL_TOKEN, fakeAuthRepo.loadTokens())
    }
}
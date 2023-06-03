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
    private lateinit var cachedSource: LoginLocalDataSource
    private lateinit var fakeRemoteSource: FakeLoginRemoteDataSource

    @Before
    fun beforeEach() {
        fakeAuthRepo = FakeAuthRepo()
        fakeRemoteSource = FakeLoginRemoteDataSource()
        cachedSource = LoginLocalDataSource.Implementation()
        repository = LoginRepositoryImpl(
            dispatchers = DispatchersProvider(),
            remoteDataSource = fakeRemoteSource,
            localDataSource = cachedSource,
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

    @Test(expected = IllegalStateException::class)
    fun `if the user doesn't send auth code the exception is throws`() = runBlocking<Unit> {
        repository.verifyIsUserExists(FakeLoginRemoteDataSource.CORRECT_AUTH_CODE)
    }

    @Test
    fun `if the user is exists tokens will be saved`() = runBlocking {
        `sendAuthCode returns true if success`()
        fakeRemoteSource.isUserExists = true
        assertTrue(repository.verifyIsUserExists(FakeLoginRemoteDataSource.CORRECT_AUTH_CODE))
        assertEquals(FakeTokenChecker.NORMAL_TOKEN, fakeAuthRepo.loadTokens())
    }

    @Test
    fun `if the user isn't exists tokens will not be saved`() = runBlocking {
        `sendAuthCode returns true if success`()
        fakeRemoteSource.isUserExists = false
        assertFalse(repository.verifyIsUserExists(FakeLoginRemoteDataSource.CORRECT_AUTH_CODE))
        assertEquals(FakeTokenChecker.EXPIRED_TOKEN, fakeAuthRepo.loadTokens())
    }

    @Test
    fun `register save tokens`() = runBlocking {
        assertEquals(FakeTokenChecker.EXPIRED_TOKEN, fakeAuthRepo.loadTokens())
        repository.register("234", "Name", "Username")
        assertEquals(FakeTokenChecker.NORMAL_TOKEN, fakeAuthRepo.loadTokens())
    }
}
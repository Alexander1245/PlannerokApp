package com.dart69.plannerokapp.core.data

import com.dart69.core.coroutines.DispatchersProvider
import com.dart69.data.response.wrapper.ResponseWrapperImpl
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

internal class AuthRepositoryImplTest {
    private lateinit var checker: FakeTokenChecker
    private lateinit var localSource: FakeAuthLocalSource
    private lateinit var remoteSource: FakeRemoteSource
    private lateinit var repository: AuthRepositoryImpl

    @Before
    fun beforeEach() {
        checker = FakeTokenChecker()
        localSource = FakeAuthLocalSource()
        remoteSource = FakeRemoteSource()
        repository = AuthRepositoryImpl(
            localDataSource = localSource,
            remoteDataSource = remoteSource,
            responseWrapper = ResponseWrapperImpl(),
            tokenMapper = TokenMapper.Companion,
            tokenChecker = checker,
            dispatchers = DispatchersProvider(),
        )
    }

    @Test(expected = IllegalStateException::class)
    fun `if tokens weren't updated yet, then loadTokens throws error`() = runBlocking<Unit> {
        repository.loadTokens()
    }

    @Test
    fun `if tokens were updated yet, then loadTokens returns them`() = runBlocking {
        val expected = FakeTokenChecker.NORMAL_TOKEN
        localSource.saveAuthToken(expected)
        assertEquals(expected, repository.loadTokens())
    }

    @Test
    fun `updateTokens updates token in localSource`() = runBlocking {
        val expected = FakeTokenChecker.NORMAL_TOKEN
        assertNull(localSource.loadAuthToken())
        repository.updateTokens(expected)
        assertEquals(expected, localSource.loadAuthToken())
    }

    @Test
    fun `if an access token is expired then the repository loads them from the remoteSource and save`() =
        runBlocking {
            val expected = FakeTokenChecker.NORMAL_TOKEN

            assertNull(localSource.loadAuthToken())
            localSource.saveAuthToken(FakeTokenChecker.EXPIRED_TOKEN)

            val actual = repository.loadTokens()

            assertEquals(expected, actual)
            assertEquals(expected, localSource.loadAuthToken())
        }

    @Test
    fun `loadTokens loads tokens from remote only once if concurrent calls are occurred`() =
        runBlocking {
            val expectedRemoteCalls = 1
            val expectedToken = FakeTokenChecker.NORMAL_TOKEN

            assertNull(localSource.loadAuthToken())
            localSource.saveAuthToken(FakeTokenChecker.EXPIRED_TOKEN)

            List(100) {
                async {
                    repository.loadTokens()
                }
            }.forEach { actualToken ->
                assertEquals(expectedToken, actualToken.await())
            }
            assertEquals(expectedRemoteCalls, remoteSource.calls.get())
        }
}
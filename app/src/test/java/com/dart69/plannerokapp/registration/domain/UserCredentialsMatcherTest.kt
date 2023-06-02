package com.dart69.plannerokapp.registration.domain

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test

class UserCredentialsMatcherTest {
    private lateinit var matcher: UserCredentialsMatcher.Implementation

    @Before
    fun beforeEach() {
        matcher = UserCredentialsMatcher.Implementation(
            UserCredentialsMatcher.AllowedCharacters(
                letters = 4,
                digits = 3,
                underscores = 2
            )
        )
    }

    @Test
    fun matches() {
        assertTrue(matcher.matchesUsername("v1_"))
        assertTrue(matcher.matchesUsername("_1v_"))
        assertTrue(matcher.matchesUsername("_1asd3f2-"))
        assertTrue(matcher.matchesUsername("1asd3f2-"))
        assertFalse(matcher.matchesUsername("и2_"))
        assertFalse(matcher.matchesUsername("a2"))
        assertFalse(matcher.matchesUsername("_1asd3f2-+"))
        assertFalse(matcher.matchesUsername("_1asd3f2_12sff"))

        assertFalse(matcher.matchesUsername("name_1_2_3"))
        assertFalse(matcher.matchesUsername("name_1-2_3"))
        assertFalse(matcher.matchesUsername("name_1-2-3"))
        assertFalse(matcher.matchesUsername("sanya_--__"))
    }

    @Test
    fun matches2() {
        val matcher = UserCredentialsMatcher.Implementation(
            UserCredentialsMatcher.AllowedCharacters(26, 10, 2)
        )
        assertTrue(matcher.matchesUsername("ssadasdadsasdasd12_-"))
        assertTrue(matcher.matchesUsername("santa123_-12"))
        assertFalse(matcher.matchesUsername("santa123456789101112"))
        assertFalse(matcher.matchesUsername("santa__-"))
        assertFalse(matcher.matchesUsername("-santa_-"))
    }
}
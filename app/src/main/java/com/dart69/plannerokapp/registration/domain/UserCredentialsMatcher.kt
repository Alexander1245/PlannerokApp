package com.dart69.plannerokapp.registration.domain

import com.dart69.plannerokapp.core.isEnglishLetter
import com.dart69.plannerokapp.core.isHyphenOrUnderscore

interface UserCredentialsMatcher {
    val allowedUsernameCharacters: AllowedCharacters

    fun matchesUsername(username: String): Boolean

    fun matchesName(name: String): Boolean

    data class AllowedCharacters(
        val letters: Int,
        val digits: Int,
        val underscores: Int,
    ) {
        val length = letters + digits + underscores
    }

    class Implementation(
        override val allowedUsernameCharacters: AllowedCharacters
    ) : UserCredentialsMatcher {
        private val maxLength = allowedUsernameCharacters.length

        override fun matchesUsername(username: String): Boolean {
            if (username.length > maxLength || username.length < MIN_LENGTH) return false
            val current = AllowedCharacters(
                letters = username.count { it.isEnglishLetter() },
                digits = username.count { it.isDigit() },
                underscores = username.count { it.isHyphenOrUnderscore() }
            )
            val isValidCount = current.letters <= allowedUsernameCharacters.letters
                    && current.digits <= allowedUsernameCharacters.digits && current.underscores <= allowedUsernameCharacters.underscores
            return current.length == username.length && isValidCount
        }

        override fun matchesName(name: String): Boolean {
            if(name.length < MIN_LENGTH || name.length > MAX_NAME_LENGTH) return false
            return name.isNotBlank() && name.trim() == name
        }
    }

    companion object {
        const val MIN_LENGTH = 3
        const val MAX_NAME_LENGTH = 30
    }
}
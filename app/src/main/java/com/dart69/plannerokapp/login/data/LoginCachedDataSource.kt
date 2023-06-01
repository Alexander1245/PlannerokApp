package com.dart69.plannerokapp.login.data

import javax.inject.Inject

interface LoginCachedDataSource {
    fun savePhone(phone: String)

    fun loadPhone(): String?

    class Implementation @Inject constructor(): LoginCachedDataSource {
        @Volatile
        private var phoneNumber: String? = null

        override fun savePhone(phone: String) {
            phoneNumber = phone
        }

        override fun loadPhone(): String? = phoneNumber
    }
}
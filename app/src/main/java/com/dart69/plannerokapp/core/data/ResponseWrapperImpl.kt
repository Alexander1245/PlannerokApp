package com.dart69.plannerokapp.core.data

import android.util.Log
import com.dart69.core.errors.NetworkTimeOutError
import com.dart69.core.errors.NoNetworkError
import com.dart69.data.response.wrapper.ResponseWrapper
import com.dart69.plannerokapp.auth.domain.ErrorMessage
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ResponseWrapperImpl @Inject constructor() : ResponseWrapper {
    override suspend fun <T> wrap(
        block: suspend () -> Response<T>,
    ): T = wrapNetworkErrors {
        val response = block()
        Log.d("ASD", response.errorBody()?.string().orEmpty())
        if (response.isSuccessful) {
            response.body()!!
        } else {
            throw ApiException(ErrorMessage(response.code(), response.message()))
        }
    }

    private suspend fun <T> wrapNetworkErrors(block: suspend () -> T): T =
        try {
            block()
        } catch (hostError: UnknownHostException) {
            throw NoNetworkError()
        } catch (timeOutError: SocketTimeoutException) {
            throw NetworkTimeOutError()
        }
}
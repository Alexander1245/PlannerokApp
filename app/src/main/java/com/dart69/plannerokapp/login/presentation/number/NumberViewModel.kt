package com.dart69.plannerokapp.login.presentation.number

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.dart69.core.errors.NoNetworkError
import com.dart69.mvvm.viewmodels.CommunicatorViewModel
import com.dart69.plannerokapp.R
import com.dart69.plannerokapp.login.domain.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class NumberViewModel @Inject constructor(
    private val repository: LoginRepository,
) : CommunicatorViewModel<NumberViewModel.State, NumberEvent>(State()) {
    private val isCodeAlreadySent = AtomicBoolean(false)
    private var job: Job? = null

    fun checkUserLogged() {
        if (job != null) return
        job = performAsync {
            if (repository.isLoggedIn()) {
                events.emit(NumberEvent.NavigateToProfile)
            }
        }
    }

    fun selectCountryNameCode(countryName: String) {
        states.update { state ->
            state.copy(
                hasSelectedCountryNameCode = countryName.isNotEmpty(),
                countryNameCode = countryName,
            )
        }
    }

    fun setPhoneNumberValidity(isValid: Boolean) {
        states.update { state ->
            state.copy(
                isButtonNextEnabled = isValid,
                formatError = if (isValid) null else R.string.invalid_number_format,
            )
        }
    }

    fun next(phoneNumber: String) {
        performAsync {
            if (isCodeAlreadySent.getAndSet(true) || repository.sendAuthCode(phoneNumber)
            ) {
                states.update { it.copy(isPhoneInputEnabled = false) }
                events.emit(NumberEvent.NavigateToCode(phoneNumber))
            }
        }
    }

    private fun performAsync(block: suspend () -> Unit) =
        viewModelScope.launch {
            try {
                states.update { it.copy(isProgressVisible = true) }
                block()
            } catch (throwable: Throwable) {
                if (throwable is NoNetworkError) {
                    events.emit(NumberEvent.ShowNetworkDialog)
                } else {
                    events.emit(NumberEvent.ShowError(throwable))
                }
            } finally {
                states.update { it.copy(isProgressVisible = false) }
            }
        }


    data class State(
        val hasSelectedCountryNameCode: Boolean = false,
        val countryNameCode: String = "",
        val isButtonNextEnabled: Boolean = false,
        @StringRes val formatError: Int? = R.string.invalid_number_format,
        val isProgressVisible: Boolean = false,
        val isPhoneInputEnabled: Boolean = true,
    )
}
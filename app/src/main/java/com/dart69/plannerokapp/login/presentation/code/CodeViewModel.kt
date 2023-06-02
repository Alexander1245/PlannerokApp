package com.dart69.plannerokapp.login.presentation.code

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dart69.core.errors.NoNetworkError
import com.dart69.mvvm.viewmodels.CommunicatorViewModel
import com.dart69.plannerokapp.login.domain.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CodeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: LoginRepository,
) : CommunicatorViewModel<CodeViewModel.State, CodeEvent>(State()) {
    private val phone = CodeFragmentArgs.fromSavedStateHandle(savedStateHandle).phone

    fun finish() {
        performAsync {
            //TODO: Bug here
            val isUserExists = repository.verifyIsUserExists(states.value.code)
            val event = if (isUserExists) {
                CodeEvent.NavigateToProfile
            } else {
                CodeEvent.NavigateToRegistration(phone)
            }
            events.emit(event)
        }
    }

    fun updateCode(code: Int) {
        states.update { state ->
            val error = if (state.code == code) state.error else null
            val isCodeValid = repository.checkCodeValidity(code)
            state.copy(
                error = error,
                isButtonFinishEnabled = isCodeValid && error == null,
                code = code,
            )
        }
    }

    private fun performAsync(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                states.update { it.copy(isProgressVisible = true) }
                block()
            } catch (throwable: Throwable) {
                if (throwable is NoNetworkError) {
                    events.emit(CodeEvent.ShowNetworkDialog)
                } else {
                    events.emit(CodeEvent.ShowError(throwable))
                }
            } finally {
                states.update { it.copy(isProgressVisible = false) }
            }
        }
    }

    data class State(
        val isProgressVisible: Boolean = false,
        val isButtonFinishEnabled: Boolean = false,
        val code: Int = 0,
        @StringRes val error: Int? = null,
    )
}
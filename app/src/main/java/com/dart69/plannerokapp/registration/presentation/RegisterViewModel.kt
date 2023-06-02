package com.dart69.plannerokapp.registration.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dart69.core.errors.NoNetworkError
import com.dart69.mvvm.strings.StringResource
import com.dart69.mvvm.strings.asStringResource
import com.dart69.mvvm.viewmodels.CommunicatorViewModel
import com.dart69.plannerokapp.R
import com.dart69.plannerokapp.login.domain.LoginRepository
import com.dart69.plannerokapp.registration.domain.UserCredentialsMatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class RegisterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: LoginRepository,
    private val matcher: UserCredentialsMatcher,
) : CommunicatorViewModel<RegisterViewModel.State, RegisterEvent>(State()) {
    private val phone = RegisterFragmentArgs.fromSavedStateHandle(savedStateHandle).phone
    private val name = MutableStateFlow("")
    private val username = MutableStateFlow("")
    private val isInProgress = MutableStateFlow(false)

    private val isUsernameCorrect = username
        .debounce(DEBOUNCE_TIMEOUT)
        .distinctUntilChanged()
        .map(matcher::matchesUsername)
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val isNameCorrect = name
        .debounce(DEBOUNCE_TIMEOUT)
        .distinctUntilChanged()
        .map(matcher::matchesName)
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    private val nameError: StringResource = R.string.name_limits.asStringResource(
        UserCredentialsMatcher.MAX_NAME_LENGTH
    )
    private val usernameError: StringResource = R.string.username_limits.asStringResource(
        matcher.allowedUsernameCharacters.letters,
        matcher.allowedUsernameCharacters.digits,
        matcher.allowedUsernameCharacters.underscores,
    )

    init {
        combine(
            isInProgress,
            isUsernameCorrect,
            isNameCorrect
        ) { isInProgress, isCorrectUserName, isCorrectName ->
            State(
                isInProgress = isInProgress,
                usernameError = if (isCorrectUserName) StringResource.Empty else usernameError,
                nameError = if (isCorrectName) StringResource.Empty else nameError,
                isRegisterButtonEnabled = isCorrectName && isCorrectUserName,
            )
        }.onEach(states::emit).launchIn(viewModelScope)
    }

    fun changeUserName(username: String) {
        this.username.value = username
    }

    fun changeName(name: String) {
        this.name.value = name
    }

    fun register() {
        performAsync {
            repository.register(phone, name.value, username.value)
            events.emit(RegisterEvent.NavigateToProfile)
        }
    }

    private fun performAsync(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                isInProgress.value = true
                block()
            } catch (throwable: Throwable) {
                if (throwable is NoNetworkError) {
                    events.emit(RegisterEvent.ShowNetworkDialog)
                } else {
                    events.emit(RegisterEvent.ShowError(throwable))
                }
            } finally {
                isInProgress.value = false
            }
        }
    }

    data class State(
        val isInProgress: Boolean = false,
        val usernameError: StringResource = StringResource.Empty,
        val nameError: StringResource = StringResource.Empty,
        val isRegisterButtonEnabled: Boolean = false,
    )

    companion object {
        const val DEBOUNCE_TIMEOUT = 150L
    }
}
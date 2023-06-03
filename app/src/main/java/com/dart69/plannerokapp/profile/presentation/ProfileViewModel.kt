package com.dart69.plannerokapp.profile.presentation

import androidx.lifecycle.viewModelScope
import com.dart69.mvvm.events.ContextEvent
import com.dart69.mvvm.viewmodels.CommunicatorViewModel
import com.dart69.plannerokapp.login.presentation.number.NumberEvent
import com.dart69.plannerokapp.profile.domain.ProfileRepository
import com.dart69.plannerokapp.profile.domain.models.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
) : CommunicatorViewModel<ProfileViewModel.State, ProfileEvent>(State()) {
    private val isProgressVisible = MutableStateFlow(false)
    private val profile = MutableStateFlow<Profile?>(null)

    init {
        viewModelScope.launch {
            performAsync(repository::initialize)
            profile.emitAll(repository.observeProfile())
        }

        combine(isProgressVisible, profile) { isInProgress, profile ->
            State(isInProgress, profile)
        }.onEach(states::emit).launchIn(viewModelScope)
    }

    fun editProfile() {
        events.launch(ProfileEvent.EditProfile(profile.value!!.details))
    }

    private suspend fun performAsync(block: suspend () -> Unit) {
        try {
            isProgressVisible.value = true
            block()
        } catch (throwable: Throwable) {
            events.emit(ProfileEvent.ShowError(throwable))
        } finally {
            isProgressVisible.value = false
        }
    }

    data class State(
        val isInProgress: Boolean = false,
        val profile: Profile? = null,
    )
}
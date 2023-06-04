package com.dart69.plannerokapp.profile.presentation

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.dart69.mvvm.events.ContextEvent
import com.dart69.mvvm.viewmodels.CommunicatorViewModel
import com.dart69.plannerokapp.R
import com.dart69.plannerokapp.core.toDateString
import com.dart69.plannerokapp.login.presentation.number.NumberEvent
import com.dart69.plannerokapp.profile.domain.ProfileRepository
import com.dart69.plannerokapp.profile.domain.models.ProfileDetails
import com.dart69.plannerokapp.registration.domain.UserCredentialsMatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val matcher: UserCredentialsMatcher,
    private val repository: ProfileRepository,
    savedStateHandle: SavedStateHandle,
) : CommunicatorViewModel<EditProfileViewModel.State, ContextEvent>(State()) {

    init {
        states.update {
            it.copy(
                details = EditProfileFragmentArgs.fromSavedStateHandle(
                    savedStateHandle
                ).details.also {
                    Log.d("ASD", it.toString())
                }
            )
        }
    }

    fun uploadPhoto(uri: String) {
        states.update { it.copy(details = it.details.copy(avatarUri = uri)) }
    }

    fun updateName(name: String) {
        states.update {
            it.copy(
                details = it.details.copy(name = name),
                nameError = if (matcher.matchesName(name)) null else R.string.name_invalid,
            )
        }
    }

    fun updateCity(city: String) {
        states.update { it.copy(details = it.details.copy(city = city)) }
    }

    fun updateInstagram(inst: String) {
        states.update { it.copy(details = it.details.copy(instagram = inst)) }
    }

    fun updateVk(vk: String) {
        states.update { it.copy(details = it.details.copy(vk = vk)) }
    }

    fun updateBirthdate(epoch: Long) {
        states.update { it.copy(details = it.details.copy(birthdate = epoch.toDateString())) }
    }

    fun updateAboutMe(aboutMe: String) {
        states.update { it.copy(details = it.details.copy(aboutMe = aboutMe)) }
    }

    fun editProfile() {
        performAsync {
            val details = states.value.details
            repository.updateProfileDetails(details)
        }
    }

    private fun performAsync(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                states.update { it.copy(isInProgress = true, isSubmitVisible = false) }
                block()
            } catch (throwable: Throwable) {
                events.emit(NumberEvent.ShowError(throwable))
            } finally {
                states.update { it.copy(isInProgress = false, isSubmitVisible = true) }
            }
        }
    }

    data class State(
        val details: ProfileDetails = ProfileDetails.EMPTY,
        @StringRes val nameError: Int? = null,
        val isInProgress: Boolean = false,
        val isSubmitVisible: Boolean = true,
    )
}
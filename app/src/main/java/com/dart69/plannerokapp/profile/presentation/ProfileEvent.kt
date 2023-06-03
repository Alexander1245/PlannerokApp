package com.dart69.plannerokapp.profile.presentation

import androidx.navigation.NavController
import com.dart69.mvvm.events.ContextEvent
import com.dart69.mvvm.events.NavigationEvent
import com.dart69.plannerokapp.login.presentation.number.NumberEvent
import com.dart69.plannerokapp.profile.domain.models.ProfileDetails

sealed interface ProfileEvent {

    data class ShowError(val throwable: Throwable): ProfileEvent, ContextEvent by NumberEvent.ShowError(throwable)

    data class EditProfile(val details: ProfileDetails): ProfileEvent, NavigationEvent {
        override fun applyOn(navController: NavController) =
            navController.navigate(
                ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(details)
            )
    }
}
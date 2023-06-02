package com.dart69.plannerokapp.login.presentation.code

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.NavController
import com.dart69.mvvm.events.ContextEvent
import com.dart69.mvvm.events.Event
import com.dart69.mvvm.events.NavigationEvent
import com.dart69.plannerokapp.login.presentation.number.NumberEvent

interface CodeEvent : Event {
    data class ShowError(val throwable: Throwable) : CodeEvent,
        ContextEvent by NumberEvent.ShowError(throwable)

    object ShowNetworkDialog : CodeEvent {
        fun applyOn(context: Context, launcher: ActivityResultLauncher<Intent>) =
            NumberEvent.ShowNetworkDialog.applyOn(context, launcher)
    }

    object NavigateToProfile : CodeEvent, NavigationEvent {
        override fun applyOn(navController: NavController) {
            TODO()
        }
    }

    data class NavigateToRegistration(
        private val phone: String
    ) : CodeEvent, NavigationEvent {
        override fun applyOn(navController: NavController) {
            navController.navigate(CodeFragmentDirections.actionCodeFragmentToRegisterFragment(phone))
        }
    }
}
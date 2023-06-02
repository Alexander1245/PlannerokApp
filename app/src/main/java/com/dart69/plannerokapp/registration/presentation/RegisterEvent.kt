package com.dart69.plannerokapp.registration.presentation

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.NavController
import com.dart69.mvvm.events.ContextEvent
import com.dart69.mvvm.events.Event
import com.dart69.mvvm.events.NavigationEvent
import com.dart69.plannerokapp.login.presentation.number.NumberEvent

sealed interface RegisterEvent : Event {

    data class ShowError(val throwable: Throwable) : RegisterEvent,
        ContextEvent by NumberEvent.ShowError(throwable)

    object ShowNetworkDialog : RegisterEvent {
        fun applyOn(context: Context, launcher: ActivityResultLauncher<Intent>) =
            NumberEvent.ShowNetworkDialog.applyOn(context, launcher)
    }

    object NavigateToProfile : RegisterEvent, NavigationEvent {
        override fun applyOn(navController: NavController) {
            TODO("Not yet implemented")
        }
    }
}
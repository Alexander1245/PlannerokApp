package com.dart69.plannerokapp.login.presentation.number

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.NavController
import com.dart69.mvvm.events.Button
import com.dart69.mvvm.events.ContextEvent
import com.dart69.mvvm.events.Event
import com.dart69.mvvm.events.NavigationEvent
import com.dart69.mvvm.events.ShowCommonDialog
import com.dart69.mvvm.events.ShowToast
import com.dart69.mvvm.strings.asStringResource
import com.dart69.plannerokapp.R

sealed interface NumberEvent : Event {

    data class ShowError(
        private val throwable: Throwable
    ) : NumberEvent, ContextEvent {
        override fun applyOn(context: Context) {
            val message = throwable.message ?: context.getString(R.string.internal_error)
            ShowToast(R.string.error_1s.asStringResource(message)).applyOn(context)
        }
    }

    object ShowNetworkDialog : NumberEvent {
        fun applyOn(context: Context, launcher: ActivityResultLauncher<Intent>) {
            ShowCommonDialog(
                title = R.string.network_error_title.asStringResource(),
                message = R.string.network_is_disabled.asStringResource(),
                positiveButton = Button(text = R.string.ok.asStringResource()) {
                    openNetworkSettings(launcher)
                }
            ).applyOn(context)
        }


        private fun openNetworkSettings(launcher: ActivityResultLauncher<Intent>) {
            val action = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                android.provider.Settings.Panel.ACTION_INTERNET_CONNECTIVITY
            } else {
                android.provider.Settings.ACTION_WIFI_SETTINGS
            }
            launcher.launch(Intent(action))
        }
    }

    data class NavigateToCode(
        private val phone: String
    ) : NumberEvent, NavigationEvent {
        override fun applyOn(navController: NavController) =
            navController.navigate(
                NumberFragmentDirections.actionNumberFragmentToCodeFragment(phone)
            )
    }
}
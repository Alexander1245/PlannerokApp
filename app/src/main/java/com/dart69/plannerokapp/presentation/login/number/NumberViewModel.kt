package com.dart69.plannerokapp.presentation.login.number

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dart69.plannerokapp.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class NumberViewModel : ViewModel() {
    private val selectedCountryCode = MutableStateFlow("")
    private val isPhoneNumberValid = MutableStateFlow(false)
    private val isProgressVisible = MutableStateFlow(false)

    val uiState = combine(
        selectedCountryCode,
        isPhoneNumberValid,
        isProgressVisible,
    ) { selected, isValid, isVisible ->
        State(
            hasSelectedCountryNameCode = selected.isNotEmpty(),
            countryNameCode = selected,
            isButtonNextEnabled = isValid,
            formatError = if(isValid) null else R.string.invalid_number_format,
            isProgressVisible = isVisible,
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, State())

    fun selectCountryNameCode(countryName: String) {
        selectedCountryCode.value = countryName
    }

    fun setPhoneNumberValidity(isValid: Boolean) {
        isPhoneNumberValid.value = isValid
    }

    fun next(phoneNumber: String) {
        isProgressVisible.value = true
    }

    data class State(
        val hasSelectedCountryNameCode: Boolean = false,
        val countryNameCode: String = "",
        val isButtonNextEnabled: Boolean = false,
        @StringRes val formatError: Int? = null,
        val isProgressVisible: Boolean = false,
    )
}
package com.dart69.plannerokapp.login.presentation.number

import com.dart69.mvvm.viewmodels.statesAsFlow
import com.dart69.plannerokapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NumberViewModelTest {
    private lateinit var repository: FakeLoginRepo
    private lateinit var viewModel: NumberViewModel

    @Before
    fun beforeEach() {
        repository = FakeLoginRepo()
        viewModel = NumberViewModel(repository)
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `test initial state`() = runBlocking {
        val expected = NumberViewModel.State(
            hasSelectedCountryNameCode = false,
            countryNameCode = "",
            isButtonNextEnabled = false,
            formatError = R.string.invalid_number_format,
            isProgressVisible = false
        )
        assertEquals(expected, viewModel.statesAsFlow().first())
    }

    @Test
    fun selectCountryNameCode() = runBlocking {
        `test initial state`()
        val expected = NumberViewModel.State(
            hasSelectedCountryNameCode = true,
            countryNameCode = "RU",
            isButtonNextEnabled = false,
            formatError = R.string.invalid_number_format,
            isProgressVisible = false,
        )
        viewModel.selectCountryNameCode("RU")
        val actual = viewModel.statesAsFlow().first()
        assertEquals(expected, actual)
    }

    @Test
    fun setPhoneNumberValidity() = runBlocking {
        selectCountryNameCode()
        val expected = NumberViewModel.State(
            hasSelectedCountryNameCode = true,
            countryNameCode = "RU",
            isButtonNextEnabled = true,
            formatError = null,
            isProgressVisible = false,
        )
        viewModel.setPhoneNumberValidity(true)
        val actual = viewModel.statesAsFlow().first()
        assertEquals(expected, actual)
    }
}
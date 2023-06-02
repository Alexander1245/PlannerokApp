package com.dart69.plannerokapp.login.presentation.number

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dart69.mvvm.screens.Screen
import com.dart69.mvvm.viewmodels.repeatOnStarted
import com.dart69.plannerokapp.R
import com.dart69.plannerokapp.core.isSkeletonVisible
import com.dart69.plannerokapp.databinding.FragmentNumberBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NumberFragment : Fragment(R.layout.fragment_number),
    Screen<FragmentNumberBinding, NumberViewModel> {
    override val binding: FragmentNumberBinding by viewBinding(FragmentNumberBinding::bind)
    override val viewModel by viewModels<NumberViewModel>()
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            next()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.run {
        super.onViewCreated(view, savedInstanceState)

        countryCodePicker.apply {
            registerCarrierNumberEditText(editTextPhoneNumber)
            setPhoneNumberValidityChangeListener(viewModel::setPhoneNumberValidity)
            setOnCountryChangeListener { viewModel.selectCountryNameCode(selectedCountryNameCode) }
            buttonNext.setOnClickListener { next() }
        }

        collectStates()
        collectEvents()
        Unit
    }

    private fun next() {
        viewModel.next(binding.countryCodePicker.fullNumberWithPlus)
    }

    private fun collectStates() = binding.run {
        repeatOnStarted(viewLifecycleOwner) {
            viewModel.collectStates { state ->
                buttonNext.isEnabled = state.isButtonNextEnabled
                inputLayoutPhoneNumber.error =
                    state.formatError?.let(this@NumberFragment::getString)
                if (state.hasSelectedCountryNameCode) {
                    countryCodePicker.setCountryForNameCode(state.countryNameCode)
                }
                editTextPhoneNumber.isEnabled = state.isPhoneInputEnabled
                root.isSkeletonVisible = state.isProgressVisible
            }
        }
    }

    private fun collectEvents() = binding.run {
        repeatOnStarted(viewLifecycleOwner) {
            viewModel.collectEvents { event ->
                when (event) {
                    is NumberEvent.ShowError -> event.applyOn(requireContext())
                    is NumberEvent.NavigateToCode -> event.applyOn(findNavController())
                    is NumberEvent.ShowNetworkDialog -> event.applyOn(requireContext(), launcher)
                }
            }
        }
    }
}
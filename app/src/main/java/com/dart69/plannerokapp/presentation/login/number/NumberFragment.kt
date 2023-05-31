package com.dart69.plannerokapp.presentation.login.number

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dart69.plannerokapp.databinding.FragmentNumberBinding
import com.dart69.plannerokapp.presentation.core.collectOnStarted
import com.dart69.plannerokapp.presentation.core.isSkeletonVisible

class NumberFragment : Fragment() {
    private lateinit var binding: FragmentNumberBinding
    private val viewModel by viewModels<NumberViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNumberBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.run {
        super.onViewCreated(view, savedInstanceState)

        countryCodePicker.registerCarrierNumberEditText(editTextPhoneNumber)
        countryCodePicker.setPhoneNumberValidityChangeListener(viewModel::setPhoneNumberValidity)
        countryCodePicker.setOnCountryChangeListener {
            viewModel.selectCountryNameCode(countryCodePicker.selectedCountryNameCode)
        }

        buttonNext.setOnClickListener { viewModel.next(countryCodePicker.fullNumberWithPlus) }

        viewModel.uiState.collectOnStarted(viewLifecycleOwner) { state ->
            buttonNext.isEnabled = state.isButtonNextEnabled
            inputLayoutPhoneNumber.error = state.formatError?.let(this@NumberFragment::getString)
            if (state.hasSelectedCountryNameCode) {
                countryCodePicker.setCountryForNameCode(state.countryNameCode)
            }
            root.isSkeletonVisible = state.isProgressVisible
        }
    }
}
package com.dart69.plannerokapp.registration.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dart69.mvvm.screens.Screen
import com.dart69.mvvm.viewmodels.repeatOnStarted
import com.dart69.plannerokapp.R
import com.dart69.plannerokapp.core.isSkeletonVisible
import com.dart69.plannerokapp.core.orNull
import com.dart69.plannerokapp.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register),
    Screen<FragmentRegisterBinding, RegisterViewModel> {
    override val binding: FragmentRegisterBinding by viewBinding(FragmentRegisterBinding::bind)
    override val viewModel: RegisterViewModel by viewModels()
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.register()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.run {
        super.onViewCreated(view, savedInstanceState)
        val args by navArgs<RegisterFragmentArgs>()
        buttonRegister.setOnClickListener { viewModel.register() }
        editTextPhone.setText(args.phone)

        editTextUserName.addTextChangedListener { it?.toString()?.let(viewModel::changeUserName) }
        editTextName.addTextChangedListener { it?.toString()?.let(viewModel::changeName) }

        collectStates()
        collectEvents()
    }

    private fun collectStates() = binding.run<FragmentRegisterBinding, Unit> {
        repeatOnStarted(viewLifecycleOwner) {
            viewModel.collectStates { state ->
                buttonRegister.isEnabled = state.isRegisterButtonEnabled
                root.isSkeletonVisible = state.isInProgress
                inputLayoutUsername.error = state.usernameError.orNull()?.asString(requireContext())
                inputLayoutName.error = state.nameError.orNull()?.asString(requireContext())
            }
        }
    }

    private fun collectEvents() = binding.run<FragmentRegisterBinding, Unit> {
        repeatOnStarted(viewLifecycleOwner) {
            viewModel.collectEvents { event ->
                val context = requireContext()
                when (event) {
                    is RegisterEvent.ShowError -> event.applyOn(context)
                    is RegisterEvent.ShowNetworkDialog -> event.applyOn(context, launcher)
                    is RegisterEvent.NavigateToProfile -> event.applyOn(findNavController())
                }
            }
        }
    }
}
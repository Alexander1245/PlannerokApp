package com.dart69.plannerokapp.login.presentation.code

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.dart69.mvvm.screens.Screen
import com.dart69.mvvm.viewmodels.repeatOnStarted
import com.dart69.plannerokapp.R
import com.dart69.plannerokapp.core.isSkeletonVisible
import com.dart69.plannerokapp.databinding.FragmentCodeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CodeFragment : Fragment(R.layout.fragment_code), Screen<FragmentCodeBinding, CodeViewModel> {
    override val binding: FragmentCodeBinding by viewBinding(FragmentCodeBinding::bind)
    override val viewModel: CodeViewModel by viewModels()
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.finish()
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.run {
        super.onViewCreated(view, savedInstanceState)

        editTextCode.addTextChangedListener { viewModel.updateCode(editTextCode.unMasked.toInt()) }
        buttonFinish.setOnClickListener { viewModel.finish() }

        collectStates()
        collectEvents()
    }

    private fun collectStates() = binding.run<FragmentCodeBinding, Unit> {
        repeatOnStarted(viewLifecycleOwner) {
            viewModel.collectStates { state ->
                buttonFinish.isEnabled = state.isButtonFinishEnabled
                inputLayoutCode.error = state.error?.let(this@CodeFragment::getString)
                root.isSkeletonVisible = state.isProgressVisible
            }
        }
    }

    private fun collectEvents() = binding.run<FragmentCodeBinding, Unit> {
        repeatOnStarted(viewLifecycleOwner) {
            viewModel.collectEvents { event ->
                val context = requireContext()
                val navController = findNavController()
                when (event) {
                    is CodeEvent.ShowError -> event.applyOn(context)
                    is CodeEvent.ShowNetworkDialog -> event.applyOn(context, launcher)
                    is CodeEvent.NavigateToProfile -> event.applyOn(navController)
                    is CodeEvent.NavigateToRegistration -> event.applyOn(navController)
                }
            }
        }
    }
}
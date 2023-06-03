package com.dart69.plannerokapp.profile.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.dart69.mvvm.screens.Screen
import com.dart69.mvvm.viewmodels.repeatOnStarted
import com.dart69.plannerokapp.R
import com.dart69.plannerokapp.core.setVisibleText
import com.dart69.plannerokapp.core.toDateString
import com.dart69.plannerokapp.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile),
    Screen<FragmentProfileBinding, ProfileViewModel> {
    override val binding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)
    override val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.run {
        super.onViewCreated(view, savedInstanceState)

        repeatOnStarted(viewLifecycleOwner) {
            viewModel.collectStates { state ->
                state.profile ?: return@collectStates
                header.textViewPhone.text = state.profile.phone
                header.textViewUsername.text = state.profile.username
                header.imageViewAvatar.load(state.profile.avatarUrl)
                details.textViewCity.setVisibleText(state.profile.city)
                details.textViewZodiac.setVisibleText(state.profile.zodiacSign)
                details.textViewBirthDate.setVisibleText(state.profile.birthDate?.toDateString())
                details.textViewAboutMe.setVisibleText(state.profile.aboutMe)
            }
        }

        repeatOnStarted(viewLifecycleOwner) {
            viewModel.collectEvents { event -> event.applyOn(requireContext()) }
        }

        Unit
    }
}
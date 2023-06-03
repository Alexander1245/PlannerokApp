package com.dart69.plannerokapp.profile.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.dart69.mvvm.screens.Screen
import com.dart69.mvvm.viewmodels.repeatOnStarted
import com.dart69.plannerokapp.R
import com.dart69.plannerokapp.core.setVisibleText
import com.dart69.plannerokapp.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile),
    Screen<FragmentProfileBinding, ProfileViewModel> {
    override val binding: FragmentProfileBinding by viewBinding(FragmentProfileBinding::bind)
    override val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.run {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setOnMenuItemClickListener {
            viewModel.editProfile()
            true
        }

        repeatOnStarted(viewLifecycleOwner) {
            viewModel.collectStates { state ->
                state.profile ?: return@collectStates
                header.textViewPhone.text = state.profile.phone
                header.textViewUsername.text = state.profile.details.username
                header.imageViewAvatar.load(state.profile.details.avatarUri) {
                    error(R.drawable.no_avatar_image)
                }
                details.textViewCity.setVisibleText(state.profile.details.city)
                details.textViewZodiac.setVisibleText(state.profile.zodiacSign)
                details.textViewBirthDate.setVisibleText(state.profile.details.birthdate)
                details.textViewAboutMe.setVisibleText(state.profile.details.aboutMe)
            }
        }

        repeatOnStarted(viewLifecycleOwner) {
            viewModel.collectEvents { event ->
                when(event) {
                    is ProfileEvent.EditProfile -> event.applyOn(findNavController())
                    is ProfileEvent.ShowError -> event.applyOn(requireContext())
                }
            }
        }

        Unit
    }
}
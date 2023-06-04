package com.dart69.plannerokapp.profile.presentation

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.dart69.mvvm.screens.Screen
import com.dart69.mvvm.viewmodels.repeatOnStarted
import com.dart69.plannerokapp.R
import com.dart69.plannerokapp.core.addListener
import com.dart69.plannerokapp.core.isSkeletonVisible
import com.dart69.plannerokapp.databinding.FragmentEditProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar


@AndroidEntryPoint
class EditProfileFragment : Fragment(R.layout.fragment_edit_profile),
    Screen<FragmentEditProfileBinding, EditProfileViewModel> {
    override val binding by viewBinding(FragmentEditProfileBinding::bind)
    override val viewModel: EditProfileViewModel by viewModels()

    private val args by navArgs<EditProfileFragmentArgs>()

    private val launcher =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.toString()?.let(viewModel::uploadPhoto)
            setAvatar(uri)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.run {
        super.onViewCreated(view, savedInstanceState)

        initializeInputs()
        initializeListeners()


        toolbar.apply {
            setOnMenuItemClickListener { editProfile() }
            setNavigationOnClickListener { findNavController().navigateUp() }
        }

        setAvatar(args.details.avatarUri)
        collectStates()
        collectEvents()
    }

    private fun setAvatar(data: Any?) = binding.run {
        imageViewAvatar.load(data) {
            error(R.drawable.no_avatar_image)
        }
    }

    private fun collectStates() = binding.run {
        repeatOnStarted(viewLifecycleOwner) {
            viewModel.collectStates { state ->
                toolbar.menu.forEach { it.isVisible = state.isSubmitVisible }
                textInputName.error = state.nameError?.let(this@EditProfileFragment::getString)
                root.isSkeletonVisible = state.isInProgress
            }
        }
    }

    private fun collectEvents() {
        repeatOnStarted(viewLifecycleOwner) {
            viewModel.collectEvents { it.applyOn(requireContext()) }
        }
    }

    private fun initializeInputs() = binding.run {
        val details = args.details

        editTextAboutMe.setText(details.aboutMe)
        editTextCity.setText(details.city)
        editTextVk.setText(details.vk)
        editTextInstagram.setText(details.instagram)
        editTextName.setText(details.name)

        editTextAboutMe.addListener(viewModel::updateAboutMe)
        editTextCity.addListener(viewModel::updateCity)
        editTextVk.addListener(viewModel::updateVk)
        editTextInstagram.addListener(viewModel::updateInstagram)
        editTextName.addListener(viewModel::updateName)
    }

    private fun initializeListeners() = binding.run {
        buttonBirthDate.setOnClickListener {
            DatePickerDialog(requireContext()).apply {
                setOnDateSetListener { _, year, month, dayOfMonth ->
                    val epoch = Calendar.getInstance().apply {
                        set(year, month, dayOfMonth)
                    }.timeInMillis
                    viewModel.updateBirthdate(epoch)
                }
            }.show()
        }

        imageViewAvatar.setOnClickListener {
            launcher.launch(
                PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    .build()
            )
        }
    }

    private fun editProfile() = binding.run {
        viewModel.updateAboutMe(editTextAboutMe.text.toString())
        viewModel.updateCity(editTextCity.text.toString())
        viewModel.updateVk(editTextVk.text.toString())
        viewModel.updateInstagram(editTextInstagram.text.toString())
        viewModel.updateName(editTextName.text.toString())
        viewModel.editProfile()
        true
    }
}
package com.dart69.plannerokapp.presentation.login.code

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dart69.plannerokapp.databinding.FragmentCodeBinding

class CodeFragment : Fragment() {
    private lateinit var binding: FragmentCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCodeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}
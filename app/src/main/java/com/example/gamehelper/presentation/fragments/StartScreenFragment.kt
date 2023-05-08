package com.example.gamehelper.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gamehelper.R
import com.example.gamehelper.databinding.StartScreenFragmentBinding


class StartScreenFragment : Fragment() {
    private var _binding: StartScreenFragmentBinding? = null

    private val binding: StartScreenFragmentBinding
        get() = _binding ?: throw RuntimeException("StartScreenFragmentBinding == null")



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = StartScreenFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            launchSingUpFragment()
        }

        binding.btnSignIn.setOnClickListener {
            launchSingInFragment()
        }
    }

    private fun launchSingUpFragment(){
        findNavController().navigate(R.id.action_startScreenFragment_to_signUpFragment)
    }

    private fun launchSingInFragment(){
        findNavController().navigate(R.id.action_startScreenFragment_to_signInFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.example.gamehelper.presentation.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gamehelper.R
import com.example.gamehelper.databinding.FragmentMainScreenBinding
import com.example.gamehelper.domain.entity.Template
import com.example.gamehelper.domain.entity.User


class MainScreenFragment : Fragment() {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding: FragmentMainScreenBinding
        get() = _binding ?: throw RuntimeException("FragmentMainScreenBinding == null")

    private val args by navArgs<MainScreenFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTest.text = "${args.user.login} $${args.user.email} ${args.user.password}"

        binding.btnTemplates.setOnClickListener { launchListOfTemplatesFragment(args.user.login, CreateTemplateFragment.FROM_LIST) }

        binding.btnMatchStories.setOnClickListener {
            launchListOfMatchesFragment(args.user.login)
        }

        binding.btnCreateGame.setOnClickListener {

            var dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_create_or_template)
            dialog.setCancelable(false)

            val btnCreate = dialog.findViewById<Button>(R.id.btnCreate)
            val btnTemplate = dialog.findViewById<Button>(R.id.btnTemplates)
            btnCreate.setOnClickListener {
                dialog.dismiss()
                launchCreateTemplateFragment(args.user.login)
            }
            btnTemplate.setOnClickListener {
                dialog.dismiss()
                launchListOfTemplatesFragment(args.user.login, CreateTemplateFragment.FROM_MAIN)
            }


            dialog.show()

        }
    }

    private fun launchListOfTemplatesFragment(login: String, mode: Int){
        findNavController().navigate(MainScreenFragmentDirections.actionMainScreenFragmentToListOfTemplatesScreenFragment(login, mode))
    }

    private fun launchCreateTemplateFragment(login: String){
        findNavController().navigate(MainScreenFragmentDirections.actionMainScreenFragmentToCreateTemplateFragment(login, CreateTemplateFragment.FROM_MAIN, Template(login, "", 0,false,false,false,false)))
    }

    private fun launchListOfMatchesFragment(login: String){
        findNavController().navigate(MainScreenFragmentDirections.actionMainScreenFragmentToListOfMatches(login))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
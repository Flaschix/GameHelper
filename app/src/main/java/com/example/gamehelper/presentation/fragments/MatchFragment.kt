package com.example.gamehelper.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.gamehelper.R
import com.example.gamehelper.databinding.FragmentMatchBinding

class MatchFragment : Fragment() {
    private var _binding: FragmentMatchBinding? = null
    private val binding: FragmentMatchBinding
        get() = _binding ?: throw RuntimeException("FragmentMatchBinding == null")

    private val args by navArgs<MatchFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTextViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpTextViews(){
        val match = args.match
        binding.tvTitle.text = match.title
        binding.tvTime.text = match.T.toString()
        binding.tvWinner.text = match.winner
    }
}
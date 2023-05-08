package com.example.gamehelper.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gamehelper.R
import com.example.gamehelper.databinding.FragmentGameStatsBinding
import com.example.gamehelper.domain.entity.Game

class GameStatsFragment : Fragment() {

    private var _binding: FragmentGameStatsBinding? = null
    private val binding: FragmentGameStatsBinding
        get() = _binding ?: throw RuntimeException("FragmentGameStatsBinding == null")

    private val args by navArgs<GameStatsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpTextViews(args.gameStats)

        binding.btnListOfMatches.setOnClickListener {
            launchListOfMatchesFragment(args.gameStats.game_name, args.userLogin)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun setUpTextViews(game: Game){
        binding.tvGameCount.text = "${getString(R.string.tv_game_count)} ${game.game_count}"
        binding.tvFastestGame.text = "${getString(R.string.tv_fastest_game)} ${ game.faster_game}"
        binding.tvSlowiestGame.text = "${getString(R.string.tv_slowliest_game)} ${ game.slower_game}"
        binding.tvAvgTime.text = "${getString(R.string.tv_avg_time)} ${ game.avg_time}"
        binding.tvBestScore.text = "${getString(R.string.tv_best_score)} ${ game.bestScore}"
    }

    private fun launchListOfMatchesFragment(game_name: String, user_name: String){
        findNavController().navigate(GameStatsFragmentDirections.actionGameStatsFragmentToListOfMatchesFragment(game_name, user_name))
    }
}
package com.example.gamehelper.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gamehelper.databinding.FragmentListOfMatchesBinding
import com.example.gamehelper.domain.entity.Game
import com.example.gamehelper.presentation.Adapters.GameAdapter
import com.example.gamehelper.presentation.ViewHolders.MainViewModel


class ListOfGamesFragment : Fragment() {
    private var _binding: FragmentListOfMatchesBinding? = null
    private val binding: FragmentListOfMatchesBinding
        get() = _binding ?: throw RuntimeException("FragmentListOfMatchesBinding == null")

    private lateinit var viewHolder: MainViewModel
    private lateinit var gameListAdapter: GameAdapter

    private val args by navArgs<ListOfGamesFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListOfMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycleView()

        viewHolder = ViewModelProvider(this)[MainViewModel::class.java]
        viewHolder.GetGamesListUseCase(args.userLogin)

        viewHolder.game.observe(viewLifecycleOwner){
            gameListAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecycleView(){
        with(binding.rvGamesList){
            gameListAdapter = GameAdapter()
            this.adapter = gameListAdapter
            this.recycledViewPool.setMaxRecycledViews(101, GameAdapter.MAX_POOL_SIZE)
        }

        setUpClickListener()
    }


    private fun setUpClickListener() {
        gameListAdapter.onGameClickListener = {
//            Toast.makeText(context, "${it.game_name} ${it.game_count} ${it.bestScore}", Toast.LENGTH_SHORT).show()
            launchGameStatsFragment(it, args.userLogin)
        }
    }

    private fun launchGameStatsFragment(game: Game, loging: String){
        findNavController().navigate(ListOfGamesFragmentDirections.actionListOfGamesToGameStatsFragment(game, loging))
    }
}
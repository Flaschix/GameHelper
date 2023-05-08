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
import com.example.gamehelper.domain.entity.Match
import com.example.gamehelper.presentation.ViewHolders.MainViewModel
import com.example.gamehelper.presentation.Adapters.MatchAdapter

class ListOfMatchesFragment : Fragment() {

    private var _binding: FragmentListOfMatchesBinding? = null

    private val binding: FragmentListOfMatchesBinding
        get() = _binding ?: throw RuntimeException("FragmentGameStatsBinding == null")

    private val args by navArgs<ListOfMatchesFragmentArgs>()

    private lateinit var viewHolder: MainViewModel
    private lateinit var matchListAdapter: MatchAdapter

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
        viewHolder.GetMatchesListUseCase(args.userName, args.gameName)

        viewHolder.match.observe(viewLifecycleOwner){
            matchListAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecycleView(){
        with(binding.rvGamesList){
            matchListAdapter = MatchAdapter()
            this.adapter = matchListAdapter
            this.recycledViewPool.setMaxRecycledViews(101, MatchAdapter.MAX_POOL_SIZE)
        }

        setUpClickListener()
    }

    private fun setUpClickListener() {
        matchListAdapter.onMatchClickListener = {
//            Toast.makeText(context, "${it.title} ${it.listOfPlayers} ${it.name}", Toast.LENGTH_SHORT).show()
            launchGameStatsFragment(it)
        }
    }

    private fun launchGameStatsFragment(match: Match){
        findNavController().navigate(ListOfMatchesFragmentDirections.actionListOfMatchesFragmentToMatchFragment(match))
    }
}
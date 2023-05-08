package com.example.gamehelper.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.gamehelper.databinding.FragmentListOfTemplatesScreenBinding
import com.example.gamehelper.domain.entity.Template
import com.example.gamehelper.presentation.ViewHolders.MainViewModel
import com.example.gamehelper.presentation.Adapters.TemplateAdapter


class ListOfTemplatesScreenFragment : Fragment() {

    private var _binding: FragmentListOfTemplatesScreenBinding? = null
    private val binding: FragmentListOfTemplatesScreenBinding
        get() = _binding ?: throw RuntimeException("FragmentListOfTemplatesScreenBinding == null")

    private lateinit var viewModel: MainViewModel
    private lateinit var templateListAdapter: TemplateAdapter

    private val args by navArgs<ListOfTemplatesScreenFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListOfTemplatesScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycleView()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.GetTemplateListUseCase(args.userLogin)

        viewModel.template.observe(viewLifecycleOwner){
            templateListAdapter.submitList(it)
        }

        if (args.mode == CreateTemplateFragment.FROM_LIST){
            binding.flaAddTemplate.setOnClickListener{
                launchCreateTemplateFragment(CreateTemplateFragment.FROM_LIST, Template(args.userLogin, "", 0,false,false,false,false))
            }
        }else binding.flaAddTemplate.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecycleView(){
        with(binding.rvTemplateList){
            templateListAdapter = TemplateAdapter()
            this.adapter = templateListAdapter
            this.recycledViewPool.setMaxRecycledViews(101, TemplateAdapter.MAX_POOL_SIZE)
        }

        setUpClickListener()
        if (args.mode == CreateTemplateFragment.FROM_LIST) setUpSwipeListener(binding.rvTemplateList)
    }

    private fun setUpClickListener() {
        templateListAdapter.onTemplateClickListener = {
//            Toast.makeText(context, "${it.name} ${it.playerCount} ${it.dice} ${it.timer} ${it.liners} ${it.table}", Toast.LENGTH_SHORT).show()
            launchCreateTemplateFragment(args.mode, Template(args.userLogin, it.name, it.playerCount, it.dice, it.timer, it.liners, it.table))
        }
    }

    private fun setUpSwipeListener(rvTemplateList: RecyclerView?){
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val template = templateListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(template)
                viewModel.GetTemplateListUseCase(args.userLogin)
            }

        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvTemplateList)

    }

    private fun launchCreateTemplateFragment(mode: Int, template: Template){
        findNavController().navigate(ListOfTemplatesScreenFragmentDirections.actionListOfTemplatesScreenFragmentToCreateTemplateFragment(args.userLogin, mode, template))
    }

    companion object {

    }
}
package com.airongomes.compassouol.overview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.airongomes.compassouol.R
import com.airongomes.compassouol.adapter.ClickListener
import com.airongomes.compassouol.adapter.RecyclerViewAdapter
import com.airongomes.compassouol.databinding.FragmentOverviewBinding

class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentOverviewBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_overview,
            container,
            false
        )

        // Cria o viewModel
        val viewModel = ViewModelProvider(this).get(OverviewViewModel::class.java)
        binding.viewModel = viewModel

        // Cria o adapter do Recyclerview
        val adapter = RecyclerViewAdapter(ClickListener { eventId ->
            viewModel.navigateToDetailFragment(eventId)
        })

        binding.recyclerview.adapter = adapter

        // Navegar para DetailFragment
        viewModel.navDetailFragment.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(
                    OverviewFragmentDirections.actionOverviewFragmentToDetailFragment(it)
                )
                viewModel.clearNavDetailFragment()
            }
        })

        // Atualizar lista do Recyclerview
        viewModel.eventList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.i("Result", "event de novo")
                // notifyDataSetChanged garante atualização correta quando carregados
                // os eventos pelo retrofit
                adapter.notifyDataSetChanged()
                adapter.submitList(it)
                binding.recyclerview.smoothScrollToPosition(0)
            }
        })

        // Usando ItemTouchHelper para permitir movimento dos itens do recyclerview
        val itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or
                        ItemTouchHelper.DOWN or ItemTouchHelper.UP,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {

                // Permite movimentar os items do recyclerview
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {

                    // Pegar as posições
                    val from = viewHolder.adapterPosition
                    val to = target.adapterPosition
                    adapter.notifyItemMoved(from, to)
                    viewModel.changeEventPosition(
                        adapter.getItem(from),
                        adapter.getItem(to))

                    return true
                }

                // Elimina o card ao desliza-lo para o lado
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    viewModel.removeEventItem(adapter.getItem(viewHolder.adapterPosition))
                    adapter.notifyItemRemoved(viewHolder.adapterPosition)
                }
            })

        // Adiciona ItemTouchHelper no Recyclerview
        itemTouchHelper.attachToRecyclerView(binding.recyclerview)

        binding.lifecycleOwner = this

        return binding.root
    }
}
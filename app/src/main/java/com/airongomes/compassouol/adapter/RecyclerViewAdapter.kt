package com.airongomes.compassouol.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airongomes.compassouol.databinding.CardviewItemBinding
import com.airongomes.compassouol.network.EventProperty

/**
 * Adapter responsável por gerenciar os itens do Recyclerview
 */
class RecyclerViewAdapter(private val clickListener: ClickListener) :
    androidx.recyclerview.widget.ListAdapter<EventProperty, ViewHolder>(DiffCalback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        return holder.bind(item, clickListener)
    }

    public override fun getItem(position: Int): EventProperty {
        return super.getItem(position)
    }
}

class ViewHolder private constructor(val binding: CardviewItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: EventProperty, clickListener: ClickListener) {
        binding.eventProperty = item
        binding.clickListener = clickListener
        binding.executePendingBindings()

    }

    companion object {
        fun from(parent: ViewGroup) : ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = CardviewItemBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(binding)
        }
    }

}

/**
 * Compara os items da lista antiga de eventos com a lista nova e faz as alterações necessárias
 */
class DiffCalback: DiffUtil.ItemCallback<EventProperty>(){
    override fun areItemsTheSame(oldItem: EventProperty, newItem: EventProperty): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: EventProperty, newItem: EventProperty): Boolean {
        return oldItem == newItem
    }
}

/**
 * Click Listener para o evento
 */
class ClickListener(val clickListener: (eventId: Int) -> Unit){
    fun onClick(event: EventProperty) = clickListener(event.id)
}
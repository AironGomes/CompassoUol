package com.airongomes.compassouol.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
* ViewModel Factory. Providencia o id do evento para o viewModel
*/
class DetailViewModelFactory(
    private val id: Int
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(id) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
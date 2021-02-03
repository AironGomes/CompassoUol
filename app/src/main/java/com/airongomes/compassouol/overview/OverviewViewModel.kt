package com.airongomes.compassouol.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airongomes.compassouol.network.Api
import com.airongomes.compassouol.network.EventProperty
import kotlinx.coroutines.launch

class OverviewViewModel : ViewModel() {

    // Enum do Status da requisição dos eventos
    enum class Status { LOADING, ERROR, DONE}

    // Livedata - Navegar para DetailFragment
    private var _navDetailFragment = MutableLiveData<Int>()
    val navDetailFragment: LiveData<Int>
        get() = _navDetailFragment

    // Livedata - Retêm a lista de eventos
    private var _eventList = MutableLiveData<MutableList<EventProperty>>()
    val eventList: LiveData<MutableList<EventProperty>> = _eventList

    // Livedata - Status da requisição do retrofit
    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status> = _status

    init {
        loadEventList()
    }

    /**
     * Navegar para DetailFragment
      */
    fun navigateToDetailFragment(eventId: Int){
        _navDetailFragment.value = eventId
    }

    /**
     * Limpar Livedata navDetailFragment
     */
    fun clearNavDetailFragment(){
        _navDetailFragment.value = null
    }

    /**
     * Remove item da LiveData eventList
     */
    fun removeEventItem(item: EventProperty) {
        _eventList.value?.remove(item)
    }

    /**
     * Change the Event Position in the eventList
     */
    fun changeEventPosition(from: EventProperty, to: EventProperty) {
        val indexFrom = _eventList.value?.indexOf(from)
        val indexTo = _eventList.value?.indexOf(to)
        if (indexFrom != null && indexTo != null) {
            _eventList.value?.set(indexFrom, to)
            _eventList.value?.set(indexTo, from)
        }
    }

    /**
     * Carregar lista de eventos
     */
    fun loadEventList() {
        viewModelScope.launch {
            _status.value = Status.LOADING
            try {
                Log.i("Result", "Trigger")
                _eventList.value = Api.retrofitService.getProperties()
                _status.value = Status.DONE
            } catch (e: Exception) {
                Log.i("Result", "FAILURE: ${e.message}")
                _status.value = Status.ERROR
            }
        }
    }
}
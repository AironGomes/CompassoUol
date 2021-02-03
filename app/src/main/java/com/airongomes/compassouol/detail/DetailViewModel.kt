package com.airongomes.compassouol.detail

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airongomes.compassouol.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response


class DetailViewModel(private val id: Int) : ViewModel() {

    // Enum do Status da requisição do evento
    enum class Status { LOADING, ERROR, DONE}

    // Livedata - Retêm o evento
    private val _event = MutableLiveData<EventProperty>()
    val event: LiveData<EventProperty> = _event

    // Livedata - Status da requisição do retrofit
    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status> = _status

    // Livedata com intent de localização
    private val _locationIntent = MutableLiveData<Intent>()
    val locationIntent: LiveData<Intent> = _locationIntent

    // Livedata com a resposta do servidor
    private val _serverResponse = MutableLiveData<Int>()
    val serverResponse: LiveData<Int> = _serverResponse

    init {
        loadEventDetail()
    }

    /**
     * Carregar evento pelo id através do Retrofit
     */
    fun loadEventDetail() {
        viewModelScope.launch {
            _status.value = Status.LOADING
            try {
                _event.value = Api.retrofitService.getPropertiesId(id)
                _status.value = Status.DONE

            } catch (e: Exception) {
                Log.i("Result", "FAILURE: ${e.message}")
                _status.value = Status.ERROR
            }
        }
    }

    /**
     * Criar intent de localização através da latitude e longitude do evento
     */
    fun getLocation() {
        val locationUri = Uri.parse("geo:0,0?q=${event.value?.latitude},${event.value?.longitude}")
        val locationIntent = Intent(Intent.ACTION_VIEW, locationUri)
        locationIntent.setPackage("com.google.android.apps.maps")
        _locationIntent.value = locationIntent
    }

    /**
     * Limpar _locationIntent LiveData
     */
    fun clearLocationIntent() {
        _locationIntent.value = null
    }

    /**
     * Limpar _serverResponse LiveData
     */
    fun clearServerResponse() {
        _serverResponse.value = null
    }

    /**
     * Registrar usuário com Post Request usando Retrofit
     * @param name, nome do usuário
     * @param email, email fornecido pelo usuário
     */
    fun registerUser(name: String, email: String) {

        val userInfo = UserInfo(id, name, email)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response : Response<ResponseBody> = Api.retrofitService.registerUser(userInfo)
                Log.i("Result", "Server Response: $response")

                withContext(Dispatchers.Main) {
                    _serverResponse.value = response.code()
                }

            } catch (e: Exception) {
                Log.i("Result", "FAILURE: ${e.message}")
            }
        }
    }

}
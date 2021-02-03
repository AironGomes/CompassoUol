package com.airongomes.compassouol.network

/**
 * Informações do Evento
 */
data class EventProperty(
    val id: Int,
    val title: String = "",
    val description: String = "",
    val date: Long? = null,
    val image: String? = null,
    val price: Double? = null,
    var longitude: Double? = null,
    var latitude: Double? = null
)

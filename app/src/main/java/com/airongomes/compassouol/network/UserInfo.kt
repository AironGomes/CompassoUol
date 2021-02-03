package com.airongomes.compassouol.network

import com.squareup.moshi.JsonClass


/**
 * Informações do usuário para serem usadas com Post Request
 */
@JsonClass(generateAdapter = true)
data class UserInfo(
    val eventId: Int,
    val name: String,
    val email: String
)
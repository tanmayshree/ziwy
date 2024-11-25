package com.anonymous.ziwy.Screens.CardsSection.Models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable

@Serializable
data class UserCardsListResponseModel(
    val statusCode: Int? = null,
    val body: ArrayList<String>? = arrayListOf(),
    val message: String? = null
)

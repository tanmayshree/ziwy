package com.anonymous.ziwy.Screens.CardsSection.Models

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserCardResponseModel(
    val statusCode: Int? = null,
    val message: String? = null
)

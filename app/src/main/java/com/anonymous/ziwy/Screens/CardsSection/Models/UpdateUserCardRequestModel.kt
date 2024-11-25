package com.anonymous.ziwy.Screens.CardsSection.Models

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserCardRequestModel(
    val mobileNumber: String? = null,
    val countryCode: String? = null,
    val cardID: List<String?>? = null
)

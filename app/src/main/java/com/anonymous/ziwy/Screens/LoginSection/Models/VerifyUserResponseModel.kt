package com.anonymous.ziwy.Screens.LoginSection.Models

import kotlinx.serialization.Serializable

@Serializable
data class VerifyUserResponseModel(
    val statusCode: Int? = null,
    val body: String? = null,
)
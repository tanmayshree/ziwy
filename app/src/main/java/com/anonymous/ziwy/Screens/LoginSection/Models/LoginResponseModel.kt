package com.anonymous.ziwy.Screens.LoginSection.Models

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseModel(
    var statusCode: Int? = null,
    var success: Boolean? = null,
    var response: Response? = null
)

@Serializable
data class Response(
    var channel: String? = null,
    var deliveryChannel: String? = null,
    var authType: String? = null,
    var requestId: String? = null
)

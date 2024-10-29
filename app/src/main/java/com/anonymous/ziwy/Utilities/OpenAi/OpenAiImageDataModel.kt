package com.anonymous.ziwy.Utilities.OpenAi

import kotlinx.serialization.Serializable

@Serializable
data class OpenAiImageDataModel(
    val coupon_code: String? = null,
    val expiry_date: String? = null,
    val brand: String? = null,
    val offer: String? = null,
    val product: List<String>? = null,
    val spend: String? = null,
    val currency: String? = null
)
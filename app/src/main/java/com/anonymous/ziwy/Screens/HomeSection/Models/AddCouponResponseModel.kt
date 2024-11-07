package com.anonymous.ziwy.Screens.HomeSection.Models

import kotlinx.serialization.Serializable

@Serializable
data class AddCouponResponseModel(
    val statusCode: Int? = null,
    val message: String? = null,
//    val couponID: String? = null,
    val body: Coupon? = null
)
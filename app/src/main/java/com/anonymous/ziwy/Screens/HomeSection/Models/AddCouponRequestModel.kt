package com.anonymous.ziwy.Screens.HomeSection.Models

import kotlinx.serialization.Serializable

@Serializable
data class AddCouponRequestModel(
    val mobileNumber: String? = null,
    val couponCode: String? = null,
    val userName: String? = null,
    val couponBrand: String? = null,
    val couponProduct: List<String>? = null,
    val couponOffer: String? = null,
    val expiryDate: String? = null,
    val redeemed: Boolean? = null,
    val minSpend: Int? = null,
    val couponSource: String? = null,
    val countryCode: String? = null,
    val couponID: String? = null
)

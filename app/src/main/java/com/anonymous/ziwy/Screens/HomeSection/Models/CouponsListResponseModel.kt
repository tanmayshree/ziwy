package com.anonymous.ziwy.Screens.HomeSection.Models

import kotlinx.serialization.Serializable

@Serializable
data class CouponsListResponseModel(
    val statusCode: Int? = null,
    val body: ArrayList<Coupon> = arrayListOf(),
    val message: String? = null
)

@Serializable
data class Coupon(
    val minSpend: Int? = null,
    val couponOffer: String? = null,
    val couponProduct: ArrayList<String>? = null,
    val couponID: String? = null,
    val expiryDate: String? = null,
    val couponBrand: String? = null,
    val couponSource: String? = null,
    val couponCode: String? = null,
    val userName: String? = null,
    val redeemed: Boolean? = null,
    val mobileNumber: String? = null,
    val countryCode: String? = null,

    //local use
    val expiryStatus: String? = null
)

package com.anonymous.ziwy.Screens.HomeSection.Models

import kotlinx.serialization.Serializable

@Serializable
data class ExtractCouponImageResponseModel(
    val couponCode: String? = null,
    val expiryDate: String? = null,
    val couponBrand: String? = null,
    val couponOffer: String? = null,
    val couponProduct: List<String>? = null,
    val minSpend: String? = null,
    val currency: String? = null,

    // {
    // "couponBrand":"Fastrack",
    // "couponCode":"FLAT60",
    // "couponOffer":"Flat 60% Off*",
    // "couponProduct":["Tees","Fastrack Sunglasses"],
    // "expiryDate":"2024-11-30",
    // "minSpend":null,
    // "currency":null
    // }
)